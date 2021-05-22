package moxy.compiler.viewstate

import com.squareup.javapoet.JavaFile
import moxy.compiler.*
import moxy.compiler.MvpCompiler.Companion.elementUtils
import moxy.compiler.MvpCompiler.Companion.messager
import moxy.compiler.viewstate.entity.*
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.StateStrategyTypeTag
import javax.lang.model.element.*
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic.Kind

class ViewInterfaceProcessor(
        private val disableEmptyStrategyCheck: Boolean,
        private val enableEmptyStrategyHelper: Boolean,
        defaultStrategy: String?
) : ElementProcessor<TypeElement, ViewInterfaceInfo> {

    private val frameworkDefaultStrategy: TypeElement
    private lateinit var viewInterfaceElement: TypeElement
    private val migrationMethods = mutableListOf<MigrationMethod>()

    init {
        if (defaultStrategy != null) {
            var localDefaultStrategy: TypeElement? = elementUtils.getTypeElement(defaultStrategy)
            if (localDefaultStrategy == null) {
                messager.printMessage(
                        Kind.ERROR,
                        "Unable to parse option '$OPTION_DEFAULT_STRATEGY'. Check $defaultStrategy exists"
                )
                localDefaultStrategy = DEFAULT_STATE_STRATEGY
            }
            frameworkDefaultStrategy = localDefaultStrategy
        } else {
            frameworkDefaultStrategy = DEFAULT_STATE_STRATEGY
        }
    }

    fun makeMigrationHelper(): JavaFile? {
        return if (enableEmptyStrategyHelper && migrationMethods.isNotEmpty()) {
            EmptyStrategyHelperGenerator.generate(migrationMethods)
        } else {
            null
        }
    }

    override fun process(element: TypeElement): ViewInterfaceInfo {
        this.viewInterfaceElement = element

        // Get methods from input interface
        val viewInterfaceMethods = getMethods(element)
        validateSuperInterfaceMethodClashes(viewInterfaceMethods)

        val methods = validateForEmptyStrategies(viewInterfaceMethods)
        addUniqueSuffixToMethodsWithTheSameName(methods)

        return ViewInterfaceInfo(element, methods.toList())
    }

    /**
     * Returns ViewMethod for each suitable method from this interface and its superinterfaces
     */
    private fun getMethods(element: TypeElement): Set<ViewInterfaceMethod> {
        // Get methods from input class
        val enclosedMethods = getEnclosedMethods(element)

        // Get methods from super interfaces
        val methodsFromSuperinterfaces = iterateInterfaces(element)

        // Combine and exclude overridden methods
        return combineMethods(enclosedMethods, methodsFromSuperinterfaces)
    }

    /**
     * Returns ViewMethod for each suitable enclosed method into this interface (but not superinterface)
     */
    private fun getEnclosedMethods(viewInterface: TypeElement): Set<ViewInterfaceMethod> {
        val viewInterfaceStrategy = getInterfaceStateStrategyType(viewInterface)
        return viewInterface
                .getNonStaticMethods()
                .map { getViewMethod(it, viewInterface, viewInterfaceStrategy) }
                .toSet()
    }

    private fun getViewMethod(
            methodElement: ExecutableElement,
            viewInterface: TypeElement,
            viewInterfaceStrategyType: TypeElement?
    ): ViewInterfaceMethod {
        if (methodElement.returnType.kind != TypeKind.VOID) {
            val message = "You are trying to generate ViewState for ${viewInterface.simpleName}. " +
                    "But ${viewInterface.simpleName} contains non-void method \"${methodElement.simpleName}\" " +
                    "with the return type of ${methodElement.returnType}."
            messager.printMessage(Kind.ERROR, message, methodElement)
        }

        val strategy: StrategyWithTag? = getStateStrategy(methodElement)
                ?: viewInterfaceStrategyType?.let { type -> StrategyWithTag(type, methodElement.defaultTag()) }

        return ViewInterfaceMethod(
                viewInterfaceElement.asDeclaredType(),
                viewInterface.asDeclaredType(),
                methodElement,
                strategy
        )
    }

    /**
     * Returns ViewMethods for all superinterfaces, or empty set if element does not have superinterfaces
     */
    private fun iterateInterfaces(
            viewInterface: TypeElement
    ): List<Set<ViewInterfaceMethod>> {
        return viewInterface.interfaces
                .map {
                    getTypeAndValidateGenerics(it)
                }
                .map {
                    // implicit recursion
                    getMethods(it)
                }
    }

    private fun getTypeAndValidateGenerics(interfaceMirror: TypeMirror): TypeElement {
        val superinterface = interfaceMirror.asTypeElement()

        val typeArguments = interfaceMirror.typeArguments
        val typeParameters = superinterface.typeParameters

        require(typeArguments.size <= typeParameters.size) {
            "Code generation for the interface ${superinterface.simpleName} failed. Simplify your generics."
        }

        return superinterface
    }

    /**
     * Combines methods, discarding duplicates from superinterface
     */
    private fun combineMethods(
            methods: Set<ViewInterfaceMethod>,
            superInterfaces: List<Set<ViewInterfaceMethod>>
    ): Set<ViewInterfaceMethod> {
        val superInterfaceMethods = combineMethodsFromSuperinterfaces(superInterfaces)
        // order is very important. Refer to Set.add() and Set.addAll() for more info
        return methods + superInterfaceMethods
    }

    private fun combineMethodsFromSuperinterfaces(
            superInterfaceMethods: List<Set<ViewInterfaceMethod>>
    ): Set<ViewInterfaceMethod> {
        val resultSet = mutableSetOf<ViewInterfaceMethod>()
        for (superInterface in superInterfaceMethods) {
            for (method in superInterface) {
                val isAdded = resultSet.add(method)
                if (!isAdded) {
                    val contained = resultSet.first { it == method }
                    contained.superInterfaceClash = method
                }
            }
        }
        return resultSet
    }

    private fun reportSuperinterfaceMethodsClash(methodA: ViewInterfaceMethod, methodB: ViewInterfaceMethod) {
        if (methodA.strategy != methodB.strategy
                && methodA.strategy != null
                && methodB.strategy != null
        ) {
            messager.printMessage(
                    Kind.ERROR,
                    "Strategy clash in superinterfaces of $viewInterfaceElement. " +
                            "Interface ${methodB.enclosingInterfaceElement.toString()} defines ${methodB.signature} " +
                            "with strategy ${methodB.strategy.strategyClass.simpleName}, " +
                            "but ${methodA.enclosingInterfaceElement.toString()} defines this method " +
                            "with strategy ${methodA.strategy.strategyClass.simpleName}. " +
                            "Override this method in $viewInterfaceElement to choose appropriate strategy",
                    viewInterfaceElement
            )
        }
    }

    private fun addUniqueSuffixToMethodsWithTheSameName(methods: List<ViewStateMethod>) {
        // Allow methods to have equal names
        val methodsCounter = mutableMapOf<String, Int>()
        for (method in methods) {
            val counter = methodsCounter[method.name] ?: 0
            if (counter > 0) {
                method.uniqueSuffix = counter.toString()
            }
            methodsCounter[method.name] = counter + 1
        }
    }

    /**
     * Returns default StateStrategyType for this [viewInterface], if specified
     */
    private fun getInterfaceStateStrategyType(viewInterface: TypeElement): TypeElement? {
        val annotation = getStateStrategy(viewInterface)
        return annotation?.strategyClass?.asTypeElement()
    }

    private fun validateSuperInterfaceMethodClashes(viewInterfaceMethods: Set<ViewInterfaceMethod>) {
        for (method in viewInterfaceMethods) {
            method.superInterfaceClash?.let { clashed ->
                reportSuperinterfaceMethodsClash(clashed, method)
            }
        }
    }

    private fun validateForEmptyStrategies(
            methods: Set<ViewInterfaceMethod>
    ): List<ViewStateMethod> {
        return methods.map { method ->
            if (method.strategy == null) {
                reportEmptyStrategy(method.methodElement)
                method.toViewMethod(StrategyWithTag(frameworkDefaultStrategy, method.methodElement.defaultTag()))
            } else {
                method.toViewMethod()
            }
        }
    }

    private fun reportEmptyStrategy(methodElement: ExecutableElement) {
        if (!disableEmptyStrategyCheck) {
            if (enableEmptyStrategyHelper) {
                migrationMethods.add(MigrationMethod(viewInterfaceElement, methodElement))
            } else {
                val message = """
                    A View method has no strategy!
                    Add @StateStrategyType annotation to this method, or to the View interface.
                    You can also specify default strategy via compiler option.
                """.trimIndent()
                messager.printMessage(Kind.ERROR, message, methodElement)
            }
        }
    }

    private val STRING_TYPE_ELEMENT by lazy {
        elementUtils.getTypeElement(String::class.java.name)
    }

    private fun getStateStrategy(element: Element): StrategyWithTag? {
        val executableElement = element as? ExecutableElement
        val enclosed = listOfNotNull(element.getAnnotationMirror(StateStrategyType::class))
                .map { it.toStrategyWithTag(executableElement) }

        val aliased = getAliasedStateStrategies(element, executableElement)

        val strategies = enclosed + aliased

        if (strategies.size > 1) {
            if (element is ExecutableElement) {
                val methodName = element.simpleName
                val methodParameters = element.parameters.joinToString {
                    it.asType().toString()
                }

                val errorMessage = """
                    There's more than one state strategy type defined for method
                    '$methodName($methodParameters)' 
                    in interface '${element.enclosingElement.asType()}'
                """.trimIndent()
                messager.printMessage(Kind.ERROR, errorMessage, element)
            } else if (element is TypeElement) {
                messager.printMessage(
                        Kind.ERROR, "There's more than one state strategy type defined for " +
                        "'${element.simpleName}'", element
                )
            }
        }

        return strategies.firstOrNull()
    }

    private fun getAliasedStateStrategies(element: Element, executableElement: ExecutableElement?) =
            element.annotationMirrors.mapNotNull { annotationMirror ->
                val aliasAnnotation = annotationMirror.annotationType.asTypeElement()
                val stateStrategyType = aliasAnnotation.getAnnotationMirror(StateStrategyType::class)
                        ?: return@mapNotNull null

                val annotationMethods = aliasAnnotation.getNonStaticMethods()

                val tagAnnotatedMethods = annotationMethods.filter {
                    it.getAnnotationMirror(StateStrategyTypeTag::class) != null
                }

                if (tagAnnotatedMethods.isEmpty()) {
                    return@mapNotNull stateStrategyType.toStrategyWithTag(executableElement)
                }

                if (tagAnnotatedMethods.size > 1) {
                    messager.printMessage(
                            Kind.ERROR, """
                                Annotation @StateStrategyTypeTag can't be applied to more than one method!
                                Annotation: ${aliasAnnotation.qualifiedName}
                            """.trimIndent(), aliasAnnotation
                    )
                    return@mapNotNull null
                }

                val tagAnnotatedMethod = tagAnnotatedMethods.first()

                if (tagAnnotatedMethod.returnType.kind != TypeKind.DECLARED) {
                    logWrongTagAnnotatedMethodReturnType(tagAnnotatedMethod, aliasAnnotation)
                    return@mapNotNull null
                }
                val tagMethodReturnType = tagAnnotatedMethod.returnType.asTypeElement()

                if (tagMethodReturnType != STRING_TYPE_ELEMENT) {
                    logWrongTagAnnotatedMethodReturnType(tagAnnotatedMethod, aliasAnnotation)
                    return@mapNotNull null
                }

                val methodName = tagAnnotatedMethod.simpleName.toString()
                val customAliasTagValue = annotationMirror.getValueAsString(methodName)

                stateStrategyType.toStrategyWithTag(
                        method = executableElement,
                        tagOverride = customAliasTagValue,
                )
            }

    private fun logWrongTagAnnotatedMethodReturnType(
            tagAnnotatedMethod: ExecutableElement,
            annotation: TypeElement,
    ) {
        val methodParameters = tagAnnotatedMethod.parameters.joinToString { it.asType().toString() }
        messager.printMessage(
                Kind.ERROR,
                """
                    Annotation @StateStrategyTypeTag can only be applied to method returning String!
                    Annotation: ${annotation.qualifiedName}
                    Method: ${tagAnnotatedMethod.simpleName}($methodParameters)
                """.trimIndent(),
                tagAnnotatedMethod,
        )
    }

    private fun AnnotationMirror.toStrategyWithTag(method: ExecutableElement? = null, tagOverride: String? = null): StrategyWithTag? {
        val strategyClassFromAnnotation = getValueAsTypeMirror(StateStrategyType::value)
                ?: return null
        val strategyType = strategyClassFromAnnotation.asTypeElement()
        val tag = tagOverride ?: getValueAsString(StateStrategyType::tag) ?: method?.defaultTag()
        ?: ""

        return StrategyWithTag(strategyType, tag)
    }

    private fun ExecutableElement.defaultTag(): String = simpleName.toString()

    companion object {
        private const val OPTION_DEFAULT_STRATEGY = MvpCompiler.DEFAULT_MOXY_STRATEGY
        private val DEFAULT_STATE_STRATEGY: TypeElement =
                elementUtils.getTypeElement(AddToEndSingleStrategy::class.java.canonicalName)
    }
}
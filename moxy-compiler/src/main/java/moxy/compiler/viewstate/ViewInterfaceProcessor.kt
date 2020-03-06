package moxy.compiler.viewstate

import com.squareup.javapoet.JavaFile
import moxy.MvpView
import moxy.compiler.ElementProcessor
import moxy.compiler.MvpCompiler
import moxy.compiler.MvpCompiler.Companion.elementUtils
import moxy.compiler.MvpCompiler.Companion.messager
import moxy.compiler.asDeclaredType
import moxy.compiler.asTypeElement
import moxy.compiler.getAnnotationMirror
import moxy.compiler.getValueAsString
import moxy.compiler.getValueAsTypeMirror
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
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
    private lateinit var viewInterfaceName: String
    private val migrationMethods = mutableListOf<MigrationMethod>()

    init {
        if (defaultStrategy != null) {
            var localDefaultStrategy: TypeElement? = elementUtils.getTypeElement(defaultStrategy)
            if (localDefaultStrategy == null) {
                messager.printMessage(
                    Kind.ERROR,
                    "Unable to parse option '$OPTION_DEFAULT_STRATEGY'. Check $defaultStrategy exists")
                localDefaultStrategy = DEFAULT_STATE_STRATEGY
            }
            frameworkDefaultStrategy = localDefaultStrategy
        } else {
            frameworkDefaultStrategy = DEFAULT_STATE_STRATEGY
        }
    }

    override fun process(element: TypeElement): ViewInterfaceInfo {
        this.viewInterfaceElement = element
        this.viewInterfaceName = element.simpleName.toString()

        // Get methods from input interface
        val methods = getMethods(element).validateAndMap(element)

        // Allow methods to have equal names
        val methodsCounter = mutableMapOf<String, Int>()
        for (method in methods) {
            val counter = methodsCounter[method.name] ?: 0
            if (counter > 0) {
                method.uniqueSuffix = counter.toString()
            }
            methodsCounter[method.name] = counter + 1
        }

        return ViewInterfaceInfo(element, methods.toList())
    }

    /**
     * Returns ViewMethod for each suitable method from this interface and its superinterfaces
     */
    private fun getMethods(element: TypeElement): List<StockViewMethod> {
        // Get methods from input class
        val enclosedMethods = getEnclosedMethods(element)

        // Get methods from super interfaces
        val methodsFromSuperinterfaces = iterateInterfaces(element)

        // Combine and exclude overridden methods
        return combineMethods(enclosedMethods, methodsFromSuperinterfaces)
    }

    fun makeMigrationHelper(): JavaFile? {
        return if (enableEmptyStrategyHelper && migrationMethods.isNotEmpty()) {
            EmptyStrategyHelperGenerator.generate(migrationMethods)
        } else {
            null
        }
    }

    /**
     * Returns ViewMethod for each suitable enclosed method into this interface (but not superinterface)
     */
    private fun getEnclosedMethods(viewInterface: TypeElement): List<StockViewMethod> {
        val defaultStrategy = getInterfaceStateStrategyType(viewInterface)
        return viewInterface.enclosedElements
            .filter {
                // ignore all but non-static methods
                it.kind == ElementKind.METHOD && !it.isStatic()
            }
            .map { getViewMethod(it as ExecutableElement, viewInterface, defaultStrategy) }
    }

    private fun getViewMethod(
            methodElement: ExecutableElement,
            viewInterface: TypeElement,
            defaultStrategy: TypeElement?
    ): StockViewMethod {
        if (methodElement.returnType.kind != TypeKind.VOID) {
            val message = "You are trying to generate ViewState for ${viewInterface.simpleName}. " +
                    "But ${viewInterface.simpleName} contains non-void method \"${methodElement.simpleName}\" " +
                    "with the return type of ${methodElement.returnType}."
            messager.printMessage(Kind.ERROR, message, methodElement)
        }

        val annotation: AnnotationMirror? = getStateStrategyTypeMirror(methodElement)

        // get strategy from annotation
        val strategyClassFromAnnotation = annotation?.getValueAsTypeMirror(StateStrategyType::value)

        val strategyClass: TypeElement? = if (strategyClassFromAnnotation != null || viewInterface.isNotMvpViewExtend()) {
            strategyClassFromAnnotation?.asTypeElement()
        } else {
            if (defaultStrategy == null && !disableEmptyStrategyCheck) {
                if (enableEmptyStrategyHelper) {
                    migrationMethods.add(MigrationMethod(viewInterface, methodElement))
                } else {
                    val message = ("A View method has no strategy! " +
                            "Method \"::${methodElement.simpleName}\". " +
                            "Add @StateStrategyType annotation to this method, or to the View interface. " +
                            "You can also specify default strategy via compiler option.")
                    messager.printMessage(Kind.ERROR, message, methodElement)
                }
            }
            defaultStrategy ?: frameworkDefaultStrategy
        }

        val tagFromAnnotation = annotation?.getValueAsString(StateStrategyType::tag)
        val methodTag: String = tagFromAnnotation ?: methodElement.simpleName.toString()

        return StockViewMethod(
            viewInterfaceElement.asDeclaredType(),
            methodElement,
            strategyClass,
            methodTag)
    }

    private fun getStateStrategyTypeMirror(methodElement: ExecutableElement): AnnotationMirror? {
        val strategies = getStateStrategyTypeMirrors(methodElement)

        if (strategies.size > 1) {
            messager.printMessage(Kind.ERROR, "There's more than one state strategy type defined for method " +
                    "'${methodElement.simpleName}(${methodElement.parameters.joinToString { it.asType().toString() }})'" +
                    " in interface '${methodElement.enclosingElement.asType()}'", methodElement)
        }

        return strategies.firstOrNull()
    }

    private fun getStateStrategyTypeMirror(typeElement: TypeElement): AnnotationMirror? {
        val strategies = getStateStrategyTypeMirrors(typeElement)

        if (strategies.size > 1) {
            messager.printMessage(Kind.ERROR, "There's more than one state strategy type defined for " +
                    "'${typeElement.simpleName}'", typeElement)
        }

        return strategies.firstOrNull()
    }

    private fun getStateStrategyTypeMirrors(element: Element): List<AnnotationMirror> {
        val enclosed = listOfNotNull(element.getAnnotationMirror(StateStrategyType::class))

        val aliased = element.annotationMirrors
            .mapNotNull { it.annotationType.asTypeElement().getAnnotationMirror(StateStrategyType::class) }

        return enclosed + aliased
    }

    /**
     * Returns ViewMethods for all superinterfaces, or empty set if element does not have superinterfaces
     */
    private fun iterateInterfaces(
        viewInterface: TypeElement
    ): List<StockViewMethod> {
        return viewInterface.interfaces
            .map {
                getTypeAndValidateGenerics(it)
            }
            .flatMap { typeElement ->
                // implicit recursion
                getMethods(typeElement)
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
     * Combines methods, comparing by [ViewMethod.equals], discarding duplicates from superinterface
     */
    private fun combineMethods(
        methods: List<StockViewMethod>,
        superInterfaceMethods: List<StockViewMethod>
    ): List<StockViewMethod> {
        return methods + superInterfaceMethods
    }

    /**
     * Returns default StateStrategyType for this [viewInterface], if specified
     */
    private fun getInterfaceStateStrategyType(viewInterface: TypeElement): TypeElement? {
        val annotation = getStateStrategyTypeMirror(viewInterface)
        val value = annotation?.getValueAsTypeMirror(StateStrategyType::value)
        return if (value != null && value.kind == TypeKind.DECLARED) {
            value.asTypeElement()
        } else {
            null
        }
    }

    private fun Element.isStatic(): Boolean {
        return modifiers.contains(Modifier.STATIC)
    }

    companion object {
        private const val OPTION_DEFAULT_STRATEGY = MvpCompiler.DEFAULT_MOXY_STRATEGY
        private val DEFAULT_STATE_STRATEGY: TypeElement =
            elementUtils.getTypeElement(AddToEndSingleStrategy::class.java.canonicalName)
    }

    class StockViewMethod(
            val targetInterfaceElement: DeclaredType,
            val element: ExecutableElement,
            val strategy: TypeElement?,
            val tag: String
    )

    private fun StockViewMethod.toViewMethod(): ViewMethod {
        if(strategy == null) throw IllegalStateException("Strategy can't be null")
        return ViewMethod(targetInterfaceElement, element, strategy, tag)
    }

    private fun TypeElement.isNotMvpViewExtend(): Boolean {
        return this.interfaces.find { parent ->
            if ((parent as DeclaredType).toString() == MvpView::class.java.canonicalName) true
            else !parent.asTypeElement().isNotMvpViewExtend()
        } == null
    }

    private fun List<StockViewMethod>.validateAndMap(viewInterface: TypeElement): Set<ViewMethod> {
        val methods = this
        return filter { method ->
            if (method.strategy == null && methods.none { it.element.simpleName == method.element.simpleName && it.strategy != null }) {
                val message = ("A View method has no strategy! " +
                        "Method \"::${method.element.simpleName}\". " +
                        "Add @StateStrategyType annotation to this method, or to the View interface. " +
                        "You can also specify default strategy via compiler option.")
                messager.printMessage(Kind.ERROR, message, viewInterface)
            }
            method.strategy != null
        }.map { it.toViewMethod() }.toSet()
    }
}
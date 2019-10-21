package moxy.compiler.viewstate

import com.squareup.javapoet.JavaFile
import moxy.compiler.ElementProcessor
import moxy.compiler.MvpCompiler.Companion.elementUtils
import moxy.compiler.MvpCompiler.Companion.messager
import moxy.compiler.getAnnotationMirror
import moxy.compiler.getValueAsString
import moxy.compiler.getValueAsTypeMirror
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.*
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic.Kind

class ViewInterfaceProcessor(
    private val disableEmptyStrategyCheck: Boolean,
    private val enableEmptyStrategyHelper: Boolean,
    defaultStrategy: String?
) : ElementProcessor<TypeElement, ViewInterfaceInfo> {

    private val frameworkDefaultStrategy: TypeElement
    private var viewInterfaceElement: TypeElement? = null
    private var viewInterfaceName: String? = null
    private val migrationMethods = mutableListOf<MigrationMethod>()

    init {
        if (defaultStrategy != null) {
            var localDefaultStrategy: TypeElement? =
                elementUtils.getTypeElement(defaultStrategy)
            if (localDefaultStrategy == null) {
                val message =
                    String.format("Unable to parse option %s. Check %s exists", defaultStrategy, defaultStrategy)
                messager.printMessage(Kind.ERROR, message)
                localDefaultStrategy = NEW_DEFAULT_STATE_STRATEGY
            }
            frameworkDefaultStrategy = localDefaultStrategy
        } else {
            frameworkDefaultStrategy = NEW_DEFAULT_STATE_STRATEGY
        }
    }

    override fun process(element: TypeElement): ViewInterfaceInfo {
        this.viewInterfaceElement = element
        this.viewInterfaceName = element.simpleName.toString()

        val methods = mutableListOf<ViewMethod>()

        val interfaceStateStrategyType = getInterfaceStateStrategyType(element)

        // Get methods for input class
        getMethods(element, interfaceStateStrategyType, ArrayList(), methods)

        // Add methods from super interfaces
        methods.addAll(iterateInterfaces(0, element, interfaceStateStrategyType, methods, ArrayList()))

        // Allow methods to have equal names
        val methodsCounter = mutableMapOf<String, Int>()
        for (method in methods) {
            var counter = methodsCounter[method.name]
            if (counter != null && counter > 0) {
                method.uniqueSuffix = counter.toString()
            } else {
                counter = 0
            }
            counter++
            methodsCounter[method.name] = counter
        }
        return ViewInterfaceInfo(element, methods)
    }

    fun makeMigrationHelper(): JavaFile? {
        return if (enableEmptyStrategyHelper && migrationMethods.isNotEmpty()) {
            EmptyStrategyHelperGenerator.generate(migrationMethods)
        } else {
            null
        }
    }

    private fun getMethods(
        typeElement: TypeElement,
        defaultStrategy: TypeElement?,
        rootMethods: List<ViewMethod>,
        superinterfacesMethods: MutableList<ViewMethod>
    ) {
        for (element in typeElement.enclosedElements) {
            // ignore all but non-static methods
            if (element.kind != ElementKind.METHOD || element.modifiers.contains(Modifier.STATIC)) {
                continue
            }

            val methodElement = element as ExecutableElement

            val method = getViewMethod(methodElement, typeElement, defaultStrategy)

            if (rootMethods.contains(method)) {
                continue
            }

            if (superinterfacesMethods.contains(method)) {
                checkStrategyAndTagEquals(method, superinterfacesMethods[superinterfacesMethods.indexOf(method)])
                continue
            }
            superinterfacesMethods.add(method)
        }
    }

    private fun getViewMethod(
        methodElement: ExecutableElement,
        typeElement: TypeElement,
        defaultStrategy: TypeElement?
    ): ViewMethod {
        if (methodElement.returnType.kind != TypeKind.VOID) {
            val message = "You are trying to generate ViewState for ${typeElement.simpleName}. " +
                    "But ${typeElement.simpleName} contains non-void method \"${methodElement.simpleName}\" " +
                    "with the return type of ${methodElement.returnType}."
            messager.printMessage(Kind.ERROR, message, methodElement)
        }

        val annotation: AnnotationMirror? = methodElement.getAnnotationMirror(StateStrategyType::class)

        // get strategy from annotation
        val strategyClassFromAnnotation = annotation?.getValueAsTypeMirror(StateStrategyType::value.name)

        val strategyClass: TypeElement = if (strategyClassFromAnnotation != null) {
            (strategyClassFromAnnotation as DeclaredType).asElement() as TypeElement
        } else {
            if (defaultStrategy == null && !disableEmptyStrategyCheck) {
                if (enableEmptyStrategyHelper) {
                    migrationMethods.add(MigrationMethod(typeElement, methodElement))
                } else {
                    val message = ("A View method has no strategy! " +
                            "Add @StateStrategyType annotation to this method, or to the View interface. " +
                            "You can also specify default strategy via compiler option.")
                    messager.printMessage(Kind.ERROR, message, methodElement)
                }
            }
            defaultStrategy ?: frameworkDefaultStrategy
        }

        // get tag from annotation
        val tagFromAnnotation = annotation?.getValueAsString(StateStrategyType::tag.name)
        val methodTag: String = tagFromAnnotation ?: methodElement.simpleName.toString()

        return ViewMethod(
            viewInterfaceElement!!.asType() as DeclaredType,
            methodElement,
            strategyClass,
            methodTag)
    }

    private fun checkStrategyAndTagEquals(
        method: ViewMethod,
        existingMethod: ViewMethod
    ) {
        val differentParts: MutableList<String> = ArrayList()
        if (existingMethod.strategy != method.strategy) {
            differentParts.add("strategies")
        }
        if (existingMethod.tag != method.tag) {
            differentParts.add("tags")
        }
        if (differentParts.isNotEmpty()) {
            val arguments = method.parameters.joinToString(", ")
            val parts = differentParts.joinToString(" and ")
            throw IllegalStateException(
                "Both ${existingMethod.enclosedClassName} and ${method.enclosedClassName} " +
                        "has method ${method.name}($arguments) with different $parts. " +
                        "Override this method in $viewInterfaceName or make $parts equal"
            )
        }
    }

    private fun iterateInterfaces(
        level: Int,
        parentElement: TypeElement,
        parentDefaultStrategy: TypeElement?,
        rootMethods: List<ViewMethod>,
        superinterfacesMethods: MutableList<ViewMethod>
    ): List<ViewMethod> {
        for (typeMirror in parentElement.interfaces) {
            val anInterface = (typeMirror as DeclaredType).asElement() as TypeElement

            val typeArguments = typeMirror.typeArguments
            val typeParameters = anInterface.typeParameters

            require(typeArguments.size <= typeParameters.size) {
                "Code generation for the interface ${anInterface.simpleName} failed. Simplify your generics."
            }

            val defaultStrategy = parentDefaultStrategy ?: getInterfaceStateStrategyType(anInterface)

            getMethods(anInterface, defaultStrategy, rootMethods, superinterfacesMethods)

            iterateInterfaces(level + 1, anInterface, defaultStrategy, rootMethods, superinterfacesMethods)
        }
        return superinterfacesMethods
    }

    private fun getInterfaceStateStrategyType(typeElement: TypeElement): TypeElement? {
        val annotation = typeElement.getAnnotationMirror(StateStrategyType::class)
        val value = annotation?.getValueAsTypeMirror(StateStrategyType::value.name)
        return if (value != null && value.kind == TypeKind.DECLARED) {
            (value as DeclaredType).asElement() as TypeElement
        } else {
            null
        }
    }

    companion object {
        private val NEW_DEFAULT_STATE_STRATEGY: TypeElement =
            elementUtils.getTypeElement(AddToEndSingleStrategy::class.java.canonicalName)
    }
}
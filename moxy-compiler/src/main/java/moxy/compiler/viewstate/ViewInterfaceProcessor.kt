package moxy.compiler.viewstate

import com.squareup.javapoet.JavaFile
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
import java.util.*
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
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
        val methods = getMethods(element)

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
    private fun getMethods(element: TypeElement): Set<ViewMethod> {
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
    private fun getEnclosedMethods(viewInterface: TypeElement): Set<ViewMethod> {
        val defaultStrategy = getInterfaceStateStrategyType(viewInterface)
        return viewInterface.enclosedElements
            .filter {
                // ignore all but non-static methods
                it.kind == ElementKind.METHOD && !it.isStatic()
            }
            .map { getViewMethod(it as ExecutableElement, viewInterface, defaultStrategy) }
            .toSet()
    }

    private fun getViewMethod(
        methodElement: ExecutableElement,
        viewInterface: TypeElement,
        defaultStrategy: TypeElement?
    ): ViewMethod {
        if (methodElement.returnType.kind != TypeKind.VOID) {
            val message = "You are trying to generate ViewState for ${viewInterface.simpleName}. " +
                    "But ${viewInterface.simpleName} contains non-void method \"${methodElement.simpleName}\" " +
                    "with the return type of ${methodElement.returnType}."
            messager.printMessage(Kind.ERROR, message, methodElement)
        }

        val annotation: AnnotationMirror? = methodElement.getAnnotationMirror(StateStrategyType::class)

        // get strategy from annotation
        val strategyClassFromAnnotation = annotation?.getValueAsTypeMirror(StateStrategyType::value)

        val strategyClass: TypeElement = if (strategyClassFromAnnotation != null) {
            strategyClassFromAnnotation.asTypeElement()
        } else {
            if (defaultStrategy == null && !disableEmptyStrategyCheck) {
                if (enableEmptyStrategyHelper) {
                    migrationMethods.add(MigrationMethod(viewInterface, methodElement))
                } else {
                    val message = ("A View method has no strategy! " +
                            "Add @StateStrategyType annotation to this method, or to the View interface. " +
                            "You can also specify default strategy via compiler option.")
                    messager.printMessage(Kind.ERROR, message, methodElement)
                }
            }
            defaultStrategy ?: frameworkDefaultStrategy
        }

        val tagFromAnnotation = annotation?.getValueAsString(StateStrategyType::tag)
        val methodTag: String = tagFromAnnotation ?: methodElement.simpleName.toString()

        return ViewMethod(
            viewInterfaceElement.asDeclaredType(),
            methodElement,
            strategyClass,
            methodTag)
    }

    /**
     * Returns ViewMethods for all superinterfaces, or empty set if element does not have superinterfaces
     */
    private fun iterateInterfaces(
        viewInterface: TypeElement
    ): Set<ViewMethod> {
        return viewInterface.interfaces
            .map {
                getTypeAndValidateGenerics(it)
            }
            .flatMapToSet {
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
     * Combines methods, comparing by [ViewMethod.equals], discarding duplicates from superinterface
     */
    private fun combineMethods(
        methods: Set<ViewMethod>,
        superInterfaceMethods: Set<ViewMethod>
    ): Set<ViewMethod> {
        return methods + superInterfaceMethods
    }

    /**
     * Returns default StateStrategyType for this [viewInterface], if specified
     */
    private fun getInterfaceStateStrategyType(viewInterface: TypeElement): TypeElement? {
        val annotation = viewInterface.getAnnotationMirror(StateStrategyType::class)
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

    private inline fun <T, R> List<T>.flatMapToSet(transform: (T) -> Iterable<R>): Set<R> {
        return flatMapTo(LinkedHashSet(), transform)
    }

    companion object {
        private const val OPTION_DEFAULT_STRATEGY = MvpCompiler.DEFAULT_MOXY_STRATEGY
        private val DEFAULT_STATE_STRATEGY: TypeElement =
            elementUtils.getTypeElement(AddToEndSingleStrategy::class.java.canonicalName)
    }
}
package moxy.compiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import moxy.InjectViewState
import moxy.compiler.presenterbinder.InjectPresenterProcessor
import moxy.compiler.presenterbinder.PresenterBinderClassGenerator
import moxy.compiler.viewstate.ViewInterfaceProcessor
import moxy.compiler.viewstate.ViewStateClassGenerator
import moxy.compiler.viewstateprovider.InjectViewStateProcessor
import moxy.compiler.viewstateprovider.ViewStateProviderClassGenerator
import moxy.presenter.InjectPresenter
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType.AGGREGATING
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.SourceVersion.latestSupported
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@AutoService(Processor::class)
@IncrementalAnnotationProcessor(AGGREGATING)
class MvpCompiler : AbstractProcessor() {

    private val defaultStrategy: String? get() = options[DEFAULT_MOXY_STRATEGY]

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        messager = processingEnv.messager
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        options = processingEnv.options
    }

    override fun getSupportedOptions(): Set<String> = setOf(
        OPTION_ENABLE_EMPTY_STRATEGY_HELPER,
        DEFAULT_MOXY_STRATEGY,
        OPTION_DISABLE_EMPTY_STRATEGY_CHECK
    )

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(
        InjectPresenter::class.java.canonicalName,
        InjectViewState::class.java.canonicalName
    )

    override fun getSupportedSourceVersion(): SourceVersion = latestSupported()

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) {
            return false
        }

        try {
            return throwableProcess(roundEnv)
        } catch (e: RuntimeException) {
            messager.printMessage(
                Diagnostic.Kind.OTHER,
                "Moxy compilation has failed. Could you copy stack trace above and write us (or open an issue on Github)?")
            e.printStackTrace()
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Moxy compilation failed; see the compiler error output for details ($e)")
        }

        return true
    }

    private fun throwableProcess(roundEnv: RoundEnvironment): Boolean {
        checkInjectors(
            roundEnv,
            PresenterInjectorRules(ElementKind.FIELD, Modifier.PUBLIC, Modifier.DEFAULT))

        val injectViewStateProcessor = InjectViewStateProcessor()
        val viewStateProviderClassGenerator = ViewStateProviderClassGenerator()

        val injectPresenterProcessor = InjectPresenterProcessor()
        val presenterBinderClassGenerator = PresenterBinderClassGenerator()

        val disableEmptyStrategyCheck = isOptionEnabled(OPTION_DISABLE_EMPTY_STRATEGY_CHECK)
        val enableEmptyStrategyHelper = isOptionEnabled(OPTION_ENABLE_EMPTY_STRATEGY_HELPER)

        val viewInterfaceProcessor = ViewInterfaceProcessor(
            disableEmptyStrategyCheck,
            enableEmptyStrategyHelper,
            defaultStrategy)
        val viewStateClassGenerator = ViewStateClassGenerator()

        processInjectors(
            roundEnv, InjectViewState::class.java, ElementKind.CLASS,
            injectViewStateProcessor, viewStateProviderClassGenerator)
        processInjectors(
            roundEnv, InjectPresenter::class.java, ElementKind.FIELD,
            injectPresenterProcessor, presenterBinderClassGenerator)

        for (usedView in injectViewStateProcessor.usedViews) {
            generateCode(
                usedView,
                ElementKind.INTERFACE,
                viewInterfaceProcessor,
                viewStateClassGenerator)
        }

        val migrationHelper = viewInterfaceProcessor.makeMigrationHelper()

        if (migrationHelper != null) {
            createSourceFile(migrationHelper)
        }

        return true
    }

    private fun isOptionEnabled(option: String): Boolean {
        return options[option]?.toBoolean() ?: false
    }

    private fun checkInjectors(roundEnv: RoundEnvironment, annotationRule: AnnotationRule) {
        for (annotatedElement in roundEnv.getElementsAnnotatedWith(InjectPresenter::class.java)) {
            annotationRule.checkAnnotation(annotatedElement)
        }

        val errorStack = annotationRule.errorStack
        if (errorStack != null && errorStack.isNotEmpty()) {
            messager.printMessage(Diagnostic.Kind.ERROR, errorStack)
        }
    }

    private fun <E : Element, R> processInjectors(
        roundEnv: RoundEnvironment,
        clazz: Class<out Annotation>,
        kind: ElementKind,
        processor: ElementProcessor<E, R>,
        classGenerator: JavaFilesGenerator<R>
    ) {
        for (element in roundEnv.getElementsAnnotatedWith(clazz)) {
            if (element.kind != kind) {
                messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "$element must be ${kind.name}, or do not annotate it with @${clazz.simpleName}",
                    element)
            }

            generateCode(element, kind, processor, classGenerator)
        }
    }

    private fun <E : Element, R> generateCode(
        element: Element,
        kind: ElementKind,
        processor: ElementProcessor<E, R>,
        classGenerator: JavaFilesGenerator<R>
    ) {
        if (element.kind != kind) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "$element must be ${kind.name}",
                element)
        }

        @Suppress("UNCHECKED_CAST")
        val result = processor.process(element as E) ?: return

        for (file in classGenerator.generate(result)) {
            createSourceFile(file)
        }
    }

    private fun createSourceFile(file: JavaFile) {
        try {
            file.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val OPTION_DISABLE_EMPTY_STRATEGY_CHECK = "disableEmptyStrategyCheck"
        const val DEFAULT_MOXY_STRATEGY = "defaultMoxyStrategy"
        private const val OPTION_ENABLE_EMPTY_STRATEGY_HELPER = "enableEmptyStrategyHelper"

        @get:JvmStatic
        lateinit var messager: Messager
            private set

        @get:JvmStatic
        lateinit var typeUtils: Types
            private set

        @get:JvmStatic
        lateinit var elementUtils: Elements
            private set

        lateinit var options: Map<String, String>
            private set
    }
}

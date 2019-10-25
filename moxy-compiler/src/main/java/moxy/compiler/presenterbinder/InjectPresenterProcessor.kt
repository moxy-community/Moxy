package moxy.compiler.presenterbinder

import com.squareup.javapoet.ClassName
import moxy.compiler.ElementProcessor
import moxy.compiler.asDeclaredType
import moxy.compiler.asTypeElement
import moxy.compiler.getAnnotationMirror
import moxy.compiler.getValueAsString
import moxy.compiler.getValueAsTypeMirror
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class InjectPresenterProcessor : ElementProcessor<VariableElement, TargetClassInfo> {

    // Cache to prevent generating PresenterBinder more than once for one class
    private val presentersContainers: MutableList<TypeElement> = mutableListOf()

    override fun process(variableElement: VariableElement): TargetClassInfo? {
        val presentersContainer: Element = variableElement.enclosingElement

        if (presentersContainer !is TypeElement) {
            throw RuntimeException(
                "Only class fields could be annotated as @InjectPresenter: $variableElement at $presentersContainer"
            )
        }

        if (presentersContainers.contains(presentersContainer)) {
            return null
        }

        presentersContainers.add(presentersContainer)

        // gather presenter fields info
        val fields = collectFields(presentersContainer)

        bindProvidersToFields(fields, collectPresenterProviders(presentersContainer))
        bindTagProvidersToFields(fields, collectTagProviders(presentersContainer))

        return TargetClassInfo(
            ClassName.get(presentersContainer),
            fields,
            findSuperPresenterContainer(presentersContainer)
        )
    }

    private fun collectFields(presentersContainer: TypeElement): List<TargetPresenterField> {
        return presentersContainer.enclosedElements
            .filter { element ->
                element.kind == ElementKind.FIELD
            }
            .mapNotNull { element ->
                val annotation = element.getAnnotationMirror(InjectPresenter::class)
                    ?: return@mapNotNull null

                // TODO: simplify?
                val fieldType: TypeMirror = element.asDeclaredType().asElement().asType()
                val fieldName = element.toString()

                val tag = annotation.getValueAsString(InjectPresenter::tag)
                val presenterId = annotation.getValueAsString(InjectPresenter::presenterId)

                TargetPresenterField(fieldType, fieldName, tag, presenterId)
            }
    }

    private fun collectPresenterProviders(presentersContainer: TypeElement): List<PresenterProviderMethod> {
        return presentersContainer.enclosedElements
            .filter { element ->
                element.kind == ElementKind.METHOD
            }
            .mapNotNull { element ->
                val annotation = element.getAnnotationMirror(ProvidePresenter::class)
                    ?: return@mapNotNull null

                element as ExecutableElement

                val methodName = element.simpleName.toString()
                val returnType = element.returnType as DeclaredType

                val tag = annotation.getValueAsString(ProvidePresenter::tag)
                val presenterId = annotation.getValueAsString(ProvidePresenter::presenterId)

                PresenterProviderMethod(returnType, methodName, tag, presenterId)
            }
    }

    private fun collectTagProviders(presentersContainer: TypeElement): List<TagProviderMethod> {
        return presentersContainer.enclosedElements
            .filter { element ->
                element.kind == ElementKind.METHOD
            }
            .mapNotNull { element ->
                val annotation = element.getAnnotationMirror(ProvidePresenterTag::class)
                    ?: return@mapNotNull null

                element as ExecutableElement

                val methodName = element.simpleName.toString()

                val presenterClass = annotation.getValueAsTypeMirror(ProvidePresenterTag::presenterClass)!!
                val presenterId = annotation.getValueAsString(ProvidePresenterTag::presenterId)

                TagProviderMethod(presenterClass, methodName, presenterId)
            }
    }

    private fun bindProvidersToFields(
        fields: List<TargetPresenterField>,
        presenterProviders: List<PresenterProviderMethod>
    ) {
        if (fields.isEmpty() || presenterProviders.isEmpty()) return

        for (presenterProvider in presenterProviders) {
            val providerTypeMirror: TypeMirror = presenterProvider.returnType.asElement().asType()

            for (field in fields) {
                if (field.type != providerTypeMirror) continue
                if (field.tag != presenterProvider.tag) continue
                if (field.presenterId != presenterProvider.presenterId) continue

                field.presenterProviderMethodName = presenterProvider.methodName
            }
        }
    }

    private fun bindTagProvidersToFields(
        fields: List<TargetPresenterField>,
        tagProviders: List<TagProviderMethod>
    ) {
        if (fields.isEmpty() || tagProviders.isEmpty()) return

        for (tagProvider in tagProviders) {
            for (field in fields) {
                if (field.type != tagProvider.presenterClass) continue
                if (field.presenterId != tagProvider.presenterId) continue

                field.presenterTagProviderMethodName = tagProvider.methodName
            }
        }
    }

    private fun findSuperPresenterContainer(typeElement: TypeElement): TypeElement? {
        var currentTypeElement: TypeElement? = typeElement
        while (currentTypeElement != null) {
            val superclass = currentTypeElement.superclass
            if (superclass.kind != TypeKind.DECLARED) break

            currentTypeElement = superclass.asTypeElement()

            if (currentTypeElement.containsInjectPresenterField()) {
                return currentTypeElement
            }
        }
        return null
    }

    private fun TypeElement.containsInjectPresenterField(): Boolean {
        return enclosedElements.any {
            it.kind == ElementKind.FIELD && it.getAnnotation(InjectPresenter::class.java) != null
        }
    }
}
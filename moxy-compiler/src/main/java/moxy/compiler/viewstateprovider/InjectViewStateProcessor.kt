package moxy.compiler.viewstateprovider

import moxy.DefaultView
import moxy.DefaultViewState
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpProcessor
import moxy.compiler.ElementProcessor
import moxy.compiler.MvpCompiler.Companion.elementUtils
import moxy.compiler.Util
import moxy.compiler.Util.fillGenerics
import moxy.compiler.getFullClassName
import javax.lang.model.element.TypeElement
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class InjectViewStateProcessor : ElementProcessor<TypeElement, PresenterInfo>() {

    val usedViews = mutableSetOf<TypeElement>()

    override fun process(element: TypeElement): PresenterInfo {
        return PresenterInfo(element, getViewStateClassName(element)!!)
    }

    private fun getViewStateClassName(typeElement: TypeElement): String? {
        getViewStateClassFromAnnotationParams(typeElement)
            .ifNotNull { return it }

        val view = getViewClassFromPresenterTypeElement(typeElement)

        val viewTypeElement = elementUtils.getTypeElement(view)
            ?: throw IllegalArgumentException("""View "$view" for $typeElement cannot be found""")

        usedViews.add(viewTypeElement)

        return Util.getFullClassName(viewTypeElement) + MvpProcessor.VIEW_STATE_SUFFIX
    }

    private fun getViewClassFromPresenterTypeElement(typeElement: TypeElement): String {
        var view = getViewClassFromAnnotationParams(typeElement)
            ?: getViewClassFromGeneric(typeElement)

        // Remove generic from view class name
        if (view.contains("<")) {
            view = view.substring(0, view.indexOf("<"))
        }
        return view
    }

    private fun getViewClassFromAnnotationParams(typeElement: TypeElement): String? {
        val annotation: InjectViewState? = typeElement.getAnnotation(InjectViewState::class.java)

        val view = catchTypeMirror { annotation?.view }
        val mvpViewClassName = view?.getFullClassName() ?: ""

        return mvpViewClassName.takeUnless {
            it.isEmpty() || it == DefaultView::class.java.name
        }
    }

    private fun getViewStateClassFromAnnotationParams(typeElement: TypeElement): String? {
        val annotation: InjectViewState? = typeElement.getAnnotation(InjectViewState::class.java)

        val viewState = catchTypeMirror { annotation?.value }
        val mvpViewStateClassName: String = viewState?.toString() ?: ""

        return mvpViewStateClassName.takeUnless {
            it.isEmpty() || it == DefaultViewState::class.java.name
        }
    }

    private fun getViewClassFromGeneric(typeElement: TypeElement): String {
        var superclass: TypeMirror = typeElement.asType()
        var parentTypes = emptyMap<String, String>()

        while (superclass.kind != TypeKind.NONE) {
            val superclassElement = (superclass as DeclaredType).asElement() as TypeElement

            val typeArguments: List<TypeMirror> = superclass.typeArguments
            val typeParameters: List<TypeParameterElement?> = superclassElement.typeParameters

            require(typeArguments.size <= typeParameters.size) {
                "Code generation for the interface ${typeElement.simpleName} failed. " +
                        "Simplify your generics. ($typeArguments vs $typeParameters)"
            }

            val types = mutableMapOf<String, String>()

            for (i in typeArguments.indices) {
                types[typeParameters[i].toString()] = fillGenerics(parentTypes, typeArguments[i])
            }

            if (superclassElement.toString() == MVP_PRESENTER_CLASS) {
                // MvpPresenter is typed only on View class
                return fillGenerics(parentTypes, typeArguments)
            }
            parentTypes = types
            superclass = superclassElement.superclass
        }
        return ""
    }

    private inline fun <T> T.ifNotNull(block: (T) -> Unit) {
        if (this != null) block(this)
    }

    private inline fun catchTypeMirror(block: () -> Unit): TypeMirror? {
        return try {
            block()
            null
        } catch (mte: MirroredTypeException) {
            mte.typeMirror
        }
    }

    companion object {
        private val MVP_PRESENTER_CLASS: String = MvpPresenter::class.java.canonicalName
    }
}
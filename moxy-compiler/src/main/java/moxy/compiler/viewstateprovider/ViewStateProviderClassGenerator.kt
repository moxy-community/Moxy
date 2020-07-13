package moxy.compiler.viewstateprovider

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import moxy.MvpProcessor
import moxy.MvpView
import moxy.ViewStateProvider
import moxy.compiler.JavaFilesGenerator
import moxy.compiler.className
import moxy.compiler.parametrizedWith
import moxy.compiler.subtypeWildcard
import moxy.compiler.toJavaFile
import moxy.viewstate.MvpViewState
import javax.lang.model.element.Modifier

class ViewStateProviderClassGenerator : JavaFilesGenerator<PresenterInfo> {

    override fun generate(presenterInfo: PresenterInfo): List<JavaFile> {
        var className = presenterInfo.name.simpleName() + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX
        var enclosingClass = presenterInfo.name.enclosingClassName()

        while (enclosingClass != null) {
            className = "${enclosingClass.simpleName()}$$className"
            enclosingClass = enclosingClass.enclosingClassName()
        }

        val typeSpec = TypeSpec
            .classBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .superclass(ViewStateProvider::class.java)
            .addMethod(presenterInfo.generateGetViewStateMethod())
            .build()
        return listOf(typeSpec.toJavaFile(presenterInfo.name))
    }

    private fun PresenterInfo.generateGetViewStateMethod() = generateGetViewStateMethod(name, viewStateName)

    private fun generateGetViewStateMethod(
        presenter: ClassName,
        viewState: ClassName?
    ): MethodSpec {
        return MethodSpec.methodBuilder("getViewState")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(MvpViewState::class.className().parametrizedWith(MvpView::class.subtypeWildcard()))
            .apply {
                if (viewState == null) {
                    addStatement("throw new RuntimeException($1S)", presenter.reflectionName() + " should have view")
                } else {
                    addStatement("return new $1T()", viewState)
                }
            }
            .build()
    }
}
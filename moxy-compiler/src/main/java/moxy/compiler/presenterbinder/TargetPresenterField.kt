package moxy.compiler.presenterbinder

import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import moxy.MvpProcessor
import javax.lang.model.type.TypeMirror

class TargetPresenterField(
    val clazz: TypeMirror,
    val name: String,
    val tag: String?,
    val presenterId: String?
) {
    private val isParametrized = TypeName.get(clazz) is ParameterizedTypeName
    val typeName: TypeName = if (isParametrized) {
        (TypeName.get(clazz) as ParameterizedTypeName).rawType
    } else {
        TypeName.get(clazz)
    }

    val generatedClassName: String get() = name.capitalize() + MvpProcessor.PRESENTER_BINDER_INNER_SUFFIX

    var presenterProviderMethodName: String? = null
    var presenterTagProviderMethodName: String? = null
}
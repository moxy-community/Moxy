package moxy.compiler.presenterbinder

import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import moxy.MvpProcessor
import javax.lang.model.type.TypeMirror

/**
 * Represents field with `@InjectPresenter` annotation.
 * `PresenterField` inner class will be generated based on data from this class.
 * [tag] and [presenterId] are annotation parameters.
 * [clazz] is field class.
 * [name] as field name.
 */
class TargetPresenterField constructor(
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

    /**
     * `@ProvidePresenter` method name if it was found for this field
     */
    var presenterProviderMethodName: String? = null

    /**
     * `@ProvidePresenterTag` method name if it was found for this field
     */
    var presenterTagProviderMethodName: String? = null
}
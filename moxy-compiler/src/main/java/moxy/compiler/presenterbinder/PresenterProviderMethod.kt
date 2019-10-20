package moxy.compiler.presenterbinder

import javax.lang.model.type.DeclaredType

/**
 * Represents method annotated with `@ProvidePresenter`.
 * [tag] and [presenterId] are annotation parameters.
 */
class PresenterProviderMethod constructor(
    val returnType: DeclaredType,
    val methodName: String,
    val tag: String?,
    val presenterId: String?
)
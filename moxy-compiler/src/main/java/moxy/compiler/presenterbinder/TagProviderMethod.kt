package moxy.compiler.presenterbinder

import javax.lang.model.type.TypeMirror

/**
 * Represents method annotated with `@ProvidePresenterTag`.
 * [presenterClass] and optional [presenterId] are annotation parameters.
 */
class TagProviderMethod constructor(
    val presenterClass: TypeMirror,
    val methodName: String,
    val presenterId: String?
)
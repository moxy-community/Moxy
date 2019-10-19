package moxy.compiler.presenterbinder

import javax.lang.model.type.DeclaredType

class PresenterProviderMethod(
    val clazz: DeclaredType,
    val name: String,
    val tag: String?,
    val presenterId: String?
)
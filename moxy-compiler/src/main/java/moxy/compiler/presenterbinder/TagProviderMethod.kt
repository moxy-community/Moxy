package moxy.compiler.presenterbinder

import javax.lang.model.type.TypeMirror

class TagProviderMethod(
    val presenterClass: TypeMirror,
    val methodName: String,
    val presenterId: String?
)
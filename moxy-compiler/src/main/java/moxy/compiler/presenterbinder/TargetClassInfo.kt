package moxy.compiler.presenterbinder

import com.squareup.javapoet.ClassName
import javax.lang.model.element.TypeElement

class TargetClassInfo constructor(
    val name: ClassName,
    val fields: List<TargetPresenterField>,
    val superPresenterBinder: TypeElement?
)
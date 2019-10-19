package moxy.compiler.presenterbinder

import com.squareup.javapoet.ClassName
import javax.lang.model.element.TypeElement

class TargetClassInfo constructor(
    name: TypeElement,
    val fields: List<TargetPresenterField>,
    val superPresenterBinder: TypeElement?
) {

    val name: ClassName = ClassName.get(name)

}
package moxy.compiler.viewstate

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

data class MigrationMethod(
    val clazz: TypeElement,
    val method: ExecutableElement
)

package moxy.compiler.viewstate

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

/**
 * Represents view interface method without strategy annotation.
 * One statement will be generated in `EmptyStrategyHelper` based on data from this class.
 */
data class MigrationMethod constructor(
    val viewInterface: TypeElement,
    val method: ExecutableElement
)

package moxy.compiler.viewstate.entity

import javax.lang.model.element.TypeElement

/**
 * Represents a strategy.
 * Parameters are mostly taken from `@StateStrategyType` annotation.
 */
data class StrategyWithTag(
    val strategyClass: TypeElement,
    val tag: String
)
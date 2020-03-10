package moxy.compiler.viewstate

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

/**
 * Represents method from view interface.
 * One `ViewCommand` inner class and one `ViewState` method will be generated based on data from this class.
 * [strategy] and [tag] are parameters of `@StateStrategyType` annotation.
 */
class ViewMethod(
    targetInterfaceElement: DeclaredType?, // enclosing interface
    element: ExecutableElement, // actual method element
    val strategy: TypeElement,
    tag: String
) : StockViewMethod(targetInterfaceElement, element, tag)
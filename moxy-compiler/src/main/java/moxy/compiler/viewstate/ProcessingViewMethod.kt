package moxy.compiler.viewstate

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

class ProcessingViewMethod(
    private val targetInterfaceElement: DeclaredType?,
    element: ExecutableElement,
    val strategy: TypeElement?,
    tag: String
) : StockViewMethod(targetInterfaceElement, element, tag) {

    fun toViewMethod(): ViewMethod {
        if (strategy == null) throw IllegalStateException("Strategy can't be null")
        return ViewMethod(targetInterfaceElement, element, strategy, tag)
    }
}
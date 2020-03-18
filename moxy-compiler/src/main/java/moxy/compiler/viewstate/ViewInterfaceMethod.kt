package moxy.compiler.viewstate

import com.squareup.javapoet.ParameterSpec
import moxy.compiler.asTypeElement
import moxy.compiler.equalsByType
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.DeclaredType

/**
 * Represents method from view interface.
 * This intermediate class transforms into [ViewStateMethod] after all methods of interface are read.
 * It can have no strategy defined in it.
 */
data class ViewInterfaceMethod constructor(
    private val interfaceElement: DeclaredType,
    val methodElement: ExecutableElement,
    val strategy: StrategyWithTag?
) {

    val parameters: List<ParameterSpec> = ViewMethodParameters.createParameters(interfaceElement, methodElement)
    val enclosedClassName: String get() = methodElement.enclosingElement.asTypeElement().qualifiedName.toString()
    private val arguments = parameters.joinToString { it.type.toString() }
    private val name: String = methodElement.simpleName.toString()
    val signature: String = "$name($arguments)"


    fun toViewMethod(): ViewStateMethod {
        return ViewStateMethod(interfaceElement, methodElement, strategy!!)
    }

    fun toViewMethod(defaultStrategy: StrategyWithTag): ViewStateMethod {
        return ViewStateMethod(interfaceElement, methodElement, defaultStrategy)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as ViewInterfaceMethod
        return name == other.name && parameters.equalsByType(other.parameters)
    }

    override fun hashCode(): Int {
        var result = 31 + name.hashCode()
        for (spec in parameters) {
            result = 31 * result + spec.type.hashCode()
        }
        return result
    }
}
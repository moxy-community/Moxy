package moxy.compiler.viewstate.entity

import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import moxy.compiler.equalsByType
import moxy.compiler.viewstate.ViewMethodParameters
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.DeclaredType

/**
 * Represents method of view state.
 * One `ViewCommand` inner class and one `ViewState` method will be generated based on data from this class.
 */
class ViewStateMethod constructor(
    targetInterfaceElement: DeclaredType, // enclosing interface
    val element: ExecutableElement, // actual method element
    val strategy: StrategyWithTag
) {
    /**
     * Joined parameter list ready to be inserted in generated code
     */
    val argumentsString: String = element.parameters.joinToString { it.simpleName }
    val name: String = element.simpleName.toString()
    val exceptions: List<TypeName> = element.thrownTypes.map { TypeName.get(it) }
    val typeVariables: List<TypeVariableName> = element.typeParameters.map { TypeVariableName.get(it) }
    val parameters: List<ParameterSpec> = ViewMethodParameters.createParameters(targetInterfaceElement, element)
    val commandClassName: String get() = name.capitalize() + uniqueSuffix + "Command"

    /**
     * Updated if interface has overloaded methods
     */
    var uniqueSuffix: String = ""


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as ViewStateMethod
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
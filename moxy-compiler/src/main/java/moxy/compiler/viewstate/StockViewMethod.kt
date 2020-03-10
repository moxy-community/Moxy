package moxy.compiler.viewstate

import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import moxy.compiler.MvpCompiler
import moxy.compiler.Util
import moxy.compiler.asTypeElement
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror

open class StockViewMethod constructor(
    targetInterfaceElement: DeclaredType?, // enclosing interface
    val element: ExecutableElement, // actual method element
    val tag: String
) {
    /**
     * Joined parameter list ready to be inserted in generated code
     */
    val argumentsString: String = element.parameters.joinToString { it.simpleName }
    val name: String = element.simpleName.toString()
    val exceptions: List<TypeName> = element.thrownTypes.map { TypeName.get(it) }
    val typeVariables: List<TypeVariableName> = element.typeParameters.map { TypeVariableName.get(it) }
    val parameters: List<ParameterSpec>

    /**
     * Updated if interface has overloaded methods
     */
    var uniqueSuffix: String = ""


    init {
        // typeUtils are used to work around generic views (https://github.com/Arello-Mobile/Moxy/issues/203)
        val executableType = MvpCompiler.typeUtils
            .asMemberOf(targetInterfaceElement, element) as ExecutableType
        val resolvedParameterTypes: List<TypeMirror> = executableType.parameterTypes

        parameters = element.parameters.zip(resolvedParameterTypes) { parameterElement, parameterType ->
            val type = TypeName.get(parameterType)
            val name = parameterElement.simpleName.toString()
            ParameterSpec.builder(type, name)
                .addModifiers(parameterElement.modifiers)
                .build()
        }
    }

    val commandClassName: String get() = name.capitalize() + uniqueSuffix + "Command"

    val enclosedClassName: String get() = element.enclosingElement.asTypeElement().qualifiedName.toString()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as StockViewMethod
        return name == other.name &&
            Util.equalsBy(
                parameters,
                other.parameters
            ) { first, second -> first.type == second.type }
    }

    override fun hashCode(): Int {
        var result = 31 + name.hashCode()
        for (spec in parameters) {
            result = 31 * result + spec.type.hashCode()
        }
        return result
    }
}
package moxy.compiler.viewstate

import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import moxy.compiler.MvpCompiler.Companion.typeUtils
import moxy.compiler.Util
import moxy.compiler.asTypeElement
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror

class ViewMethod constructor(
    targetInterfaceElement: DeclaredType?,
    val element: ExecutableElement,
    val strategy: TypeElement,
    val tag: String
) {
    val name: String = element.simpleName.toString()
    val argumentsString: String = element.parameters.joinToString { it.simpleName }
    val exceptions: List<TypeName> = element.thrownTypes.map { TypeName.get(it) }
    val typeVariables: List<TypeVariableName> = element.typeParameters.map { TypeVariableName.get(it) }
    val parameterSpecs: List<ParameterSpec>

    var uniqueSuffix: String = ""


    init {
        // typeUtils are used to work around generic views (https://github.com/Arello-Mobile/Moxy/issues/203)
        val executableType = typeUtils.asMemberOf(targetInterfaceElement, element) as ExecutableType
        val resolvedParameterTypes: List<TypeMirror> = executableType.parameterTypes

        parameterSpecs = element.parameters.zip(resolvedParameterTypes) { parameterElement, parameterType ->
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
        other as ViewMethod
        return name == other.name &&
                Util.equalsBy(
                    parameterSpecs,
                    other.parameterSpecs
                ) { first, second -> first.type == second.type }
    }

    override fun hashCode(): Int {
        var result = 31 + name.hashCode()
        for (spec in parameterSpecs) {
            result = 31 * result + spec.type.hashCode()
        }
        return result
    }
}
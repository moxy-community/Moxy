package moxy.compiler.viewstate

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import java.util.stream.Collectors
import javax.lang.model.element.TypeElement
import javax.lang.model.element.TypeParameterElement

class ViewInterfaceInfo(
    val element: TypeElement,
    val methods: List<ViewMethod>
) {
    val name: ClassName = ClassName.get(element)
    val typeVariables: List<TypeVariableName> = element.typeParameters.map { element-> TypeVariableName.get(element) }

    val nameWithTypeVariables: TypeName
        get() {
            return if (typeVariables.isEmpty()) {
                name
            } else {
                ParameterizedTypeName.get(name, *typeVariables.toTypedArray())
            }
        }
}
package moxy.compiler.viewstate

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import moxy.compiler.parametrizedWith
import javax.lang.model.element.TypeElement

/**
 * Represents interface, that was used as view for at least one presenter.
 * `ViewState` will be generated based on data from this class.
 */
class ViewInterfaceInfo constructor(
    val element: TypeElement,
    val methods: List<ViewMethod>
) {
    val name: ClassName = ClassName.get(element)
    val typeVariables: List<TypeVariableName> = element.typeParameters.map { TypeVariableName.get(it) }

    val nameWithTypeVariables: TypeName
        get() {
            return if (typeVariables.isEmpty()) {
                name
            } else {
                name.parametrizedWith(typeVariables)
            }
        }
}
package moxy.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

fun DeclaredType.asTypeElement() = asElement() as TypeElement
fun Element.asTypeElement() = this as TypeElement

fun ClassName.parametrizedWith(type: TypeName) = ParameterizedTypeName.get(this, type)
fun ClassName.parametrizedWith(types: List<TypeVariableName>): ParameterizedTypeName {
    return ParameterizedTypeName.get(this, *types.toTypedArray())
}
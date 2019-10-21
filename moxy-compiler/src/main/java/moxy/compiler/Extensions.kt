package moxy.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import com.squareup.javapoet.WildcardTypeName
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

fun DeclaredType.asTypeElement() = asElement() as TypeElement
fun Element.asTypeElement() = this as TypeElement

fun ClassName.parametrizedWith(type: TypeName) = ParameterizedTypeName.get(this, type)
fun ClassName.parametrizedWith(types: List<TypeVariableName>): ParameterizedTypeName {
    return ParameterizedTypeName.get(this, *types.toTypedArray())
}

fun KClass<*>.className(): ClassName = ClassName.get(java)
fun KClass<*>.subtypeWildcard(): WildcardTypeName = WildcardTypeName.subtypeOf(java)
fun KClass<*>.supertypeWildcard(): WildcardTypeName = WildcardTypeName.supertypeOf(java)
fun ClassName.supertypeWildcard(): WildcardTypeName = WildcardTypeName.supertypeOf(this)

fun TypeSpec.toJavaFile(className: ClassName): JavaFile = toJavaFile(className.packageName())
fun TypeSpec.toJavaFile(packageName: String): JavaFile {
    return JavaFile.builder(packageName, this)
        .indent("\t")
        .build()
}

fun <T : Annotation> Element.getAnnotationMirror(type: KClass<T>): AnnotationMirror? {
    return Util.getAnnotation(this, type.java.name)
}

// Pass property name, like StateStrategyType::value.name
fun AnnotationMirror.getValueAsString(property: String): String? {
    return Util.getAnnotationValueAsString(this, property)
}

// Pass property name, like StateStrategyType::value.name
fun AnnotationMirror.getValueAsTypeMirror(property: String): TypeMirror? {
    return Util.getAnnotationValueAsTypeMirror(this, property)
}

fun TypeMirror.getFullClassName(): String = Util.getFullClassName(this)
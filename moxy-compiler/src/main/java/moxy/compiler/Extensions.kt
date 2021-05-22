package moxy.compiler

import com.squareup.javapoet.*
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * TypeMirror must be of kind TypeKind.DECLARED
 */
@OptIn(ExperimentalContracts::class)
fun TypeMirror.asTypeElement(): TypeElement {
    contract {
        returns() implies (this@asTypeElement is DeclaredType)
    }
    return (this as DeclaredType).asElement() as TypeElement
}

fun Element.asTypeElement() = this as TypeElement
fun Element.asDeclaredType() = this.asType() as DeclaredType

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

// Pass property name, like StateStrategyType::value
fun AnnotationMirror.getValueAsString(property: KProperty1<*, *>): String? {
    return Util.getAnnotationValueAsString(this, property.name)
}

// Pass property name string
fun AnnotationMirror.getValueAsString(propertyName: String): String? {
    return Util.getAnnotationValueAsString(this, propertyName)
}

// Pass property name, like StateStrategyType::value
fun AnnotationMirror.getValueAsTypeMirror(property: KProperty1<*, *>): TypeMirror? {
    return Util.getAnnotationValueAsTypeMirror(this, property.name)
}

fun TypeMirror.getFullClassName(): String = Util.getFullClassName(this)

fun List<ParameterSpec>.equalsByType(other: List<ParameterSpec>): Boolean {
    return Util.equalsBy(this, other) { first, second ->
        first.type == second.type
    }
}

fun TypeElement.getNonStaticMethods(): Set<ExecutableElement> {
    return enclosedElements
            .filter { it.kind == ElementKind.METHOD && !it.isStatic() }
            .map { it as ExecutableElement }
            .toSet()
}

private fun Element.isStatic(): Boolean {
    return modifiers.contains(Modifier.STATIC)
}
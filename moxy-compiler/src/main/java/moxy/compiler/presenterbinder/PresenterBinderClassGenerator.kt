package moxy.compiler.presenterbinder

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import moxy.MvpPresenter
import moxy.MvpProcessor
import moxy.PresenterBinder
import moxy.compiler.JavaFilesGenerator
import moxy.compiler.Util
import moxy.compiler.className
import moxy.compiler.parametrizedWith
import moxy.compiler.subtypeWildcard
import moxy.compiler.supertypeWildcard
import moxy.compiler.toJavaFile
import moxy.presenter.PresenterField
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

/**
 * 18.12.2015
 *
 * Generates PresenterBinder for a class annotated with @InjectPresenters
 */
class PresenterBinderClassGenerator : JavaFilesGenerator<TargetClassInfo> {

    override fun generate(targetClassInfo: TargetClassInfo): List<JavaFile> {
        val targetClassName = targetClassInfo.name
        val fields = targetClassInfo.fields
        val superPresenterBinder = targetClassInfo.superPresenterBinder

        val containerSimpleName = targetClassName.simpleNames().joinToString("$")

        val classBuilder = TypeSpec
            .classBuilder(containerSimpleName + MvpProcessor.PRESENTER_BINDER_SUFFIX)
            .addModifiers(Modifier.PUBLIC)
            .superclass(PresenterBinder::class.className().parametrizedWith(targetClassName))

        for (field in fields) {
            classBuilder.addType(generatePresenterBinderClass(field, targetClassName))
        }

        classBuilder.addMethod(
            generateGetPresentersMethod(
                fields,
                targetClassName,
                superPresenterBinder))

        return listOf(classBuilder.build().toJavaFile(targetClassName))
    }

    private fun generateGetPresentersMethod(
        fields: List<TargetPresenterField>,
        containerClassName: ClassName,
        superPresenterBinder: TypeElement?
    ): MethodSpec {

        val builder = MethodSpec.methodBuilder("getPresenterFields")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(
                List::class.className().parametrizedWith(
                    PresenterField::class.className().parametrizedWith(
                        containerClassName.supertypeWildcard()
                    )
                )
            )

        builder.addStatement(
            "$1T<$2T<$3T>> presenters = new $4T<>($5L)",
            List::class.java,
            PresenterField::class.java,
            containerClassName.supertypeWildcard(),
            ArrayList::class.java,
            fields.size)

        for (field in fields) {
            builder.addStatement("presenters.add(new $1L())", field.generatedClassName)
        }

        if (superPresenterBinder != null) {
            builder.addStatement(
                "presenters.addAll(new $1L().getPresenterFields())",
                superPresenterBinder.qualifiedName.toString() + MvpProcessor.PRESENTER_BINDER_SUFFIX)
        }

        builder.addStatement("return presenters")

        return builder.build()
    }

    private fun generatePresenterBinderClass(
        field: TargetPresenterField,
        targetClassName: ClassName
    ): TypeSpec {

        val tag = field.tag ?: field.name

        return TypeSpec.classBuilder(field.generatedClassName)
            .addModifiers(Modifier.PUBLIC)
            .superclass(PresenterField::class.className().parametrizedWith(targetClassName))
            .addMethod(generatePresenterBinderConstructor(field, tag))
            .addMethod(generateBindMethod(field, targetClassName))
            .addMethod(generateProvidePresenterMethod(field, targetClassName))
            .addOptionalMethod(generateGetTagMethod(field.presenterTagProviderMethodName, targetClassName))
            .build()
    }

    private fun generateGetTagMethod(
        tagProviderMethodName: String?,
        targetClassName: ClassName
    ): MethodSpec? {
        tagProviderMethodName ?: return null
        return MethodSpec.methodBuilder("getTag")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(String::class.java)
            .addParameter(targetClassName, "delegated")
            .addStatement("return String.valueOf(delegated.$1L())", tagProviderMethodName)
            .build()
    }

    private fun generatePresenterBinderConstructor(
        field: TargetPresenterField,
        tag: String
    ): MethodSpec {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super($1S, $2S, $3T.class)", tag, field.presenterId, field.typeName)
            .build()
    }

    private fun generateBindMethod(
        field: TargetPresenterField,
        targetClassName: ClassName
    ): MethodSpec {
        return MethodSpec.methodBuilder("bind")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(targetClassName, "target")
            .addParameter(MvpPresenter::class.java, "presenter")
            .addStatement("target.$1L = ($2T) presenter", field.name, field.typeName)
            .build()
    }

    private fun generateProvidePresenterMethod(
        field: TargetPresenterField,
        targetClassName: ClassName
    ): MethodSpec {
        val builder: MethodSpec.Builder =
            MethodSpec.methodBuilder("providePresenter")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(MvpPresenter::class.className().parametrizedWith(Any::class.subtypeWildcard()))
                .addParameter(targetClassName, "delegated")

        if (field.presenterProviderMethodName != null) {
            builder.addStatement("return delegated.$1L()", field.presenterProviderMethodName)
        } else {
            val hasEmptyConstructor = Util.hasEmptyConstructor((field.type as DeclaredType).asElement() as TypeElement)

            if (hasEmptyConstructor) {
                builder.addStatement("return new $1T()", field.typeName)
            } else {
                builder.addStatement(
                    "throw new $1T($2S + $3S)",
                    IllegalStateException::class.java,
                    field.type,
                    " hasn't got a default constructor. You can apply @ProvidePresenter to a method which will "
                            + "construct Presenter. Otherwise you can add empty constructor to presenter.")
            }
        }

        return builder.build()
    }

    private fun TypeSpec.Builder.addOptionalMethod(methodSpec: MethodSpec?): TypeSpec.Builder {
        return if (methodSpec != null) {
            addMethod(methodSpec)
        } else {
            this
        }
    }
}
package moxy.compiler.presenterbinder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import moxy.MvpPresenter;
import moxy.MvpProcessor;
import moxy.PresenterBinder;
import moxy.compiler.ExtensionsKt;
import moxy.compiler.JavaFilesGenerator;
import moxy.compiler.Util;
import moxy.presenter.PresenterField;

/**
 * 18.12.2015
 * <p>
 * Generates PresenterBinder for a class annotated with &#64;InjectPresenters
 * <p>
 * An example: a class with a single injected presenter
 * <pre>
 * {@code
 *
 * &#64;InjectPresenters
 * public class Sample extends MvpActivity implements MyView
 * {
 *
 * &#64;InjectPresenter
 * example.MyPresenter mMyPresenter;
 *
 * }
 *
 * }
 * </pre>
 * <p>
 * PresenterBinderClassGenerator generates PresenterBinder
 */
public final class PresenterBinderClassGenerator extends JavaFilesGenerator<TargetClassInfo> {

    private static MethodSpec generateGetPresentersMethod(
        List<TargetPresenterField> fields,
        ClassName containerClassName,
        TypeElement superPresenterBinder) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getPresenterFields")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(
                ClassName.get(List.class), ParameterizedTypeName.get(
                    ClassName.get(PresenterField.class),
                    WildcardTypeName.supertypeOf(containerClassName))));

        builder.addStatement("$T<$T<$T>> presenters = new $T<>($L)",
            List.class, PresenterField.class, WildcardTypeName.supertypeOf(containerClassName),
            ArrayList.class, fields.size());

        for (TargetPresenterField field : fields) {
            builder.addStatement("presenters.add(new $L())", field.getGeneratedClassName());
        }

        if (superPresenterBinder != null) {
            builder.addStatement("presenters.addAll(new $L().getPresenterFields())",
                superPresenterBinder.getQualifiedName() + MvpProcessor.PRESENTER_BINDER_SUFFIX);
        }

        builder.addStatement("return presenters");

        return builder.build();
    }

    private static TypeSpec generatePresenterBinderClass(TargetPresenterField field,
        ClassName targetClassName) {
        String tag = field.getTag();
        if (tag == null) {
            tag = field.getName();
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(field.getGeneratedClassName())
            .addModifiers(Modifier.PUBLIC)
            .superclass(
                ParameterizedTypeName.get(ClassName.get(PresenterField.class), targetClassName))
            .addMethod(generatePresenterBinderConstructor(field, tag))
            .addMethod(generateBindMethod(field, targetClassName))
            .addMethod(generateProvidePresenterMethod(field, targetClassName));

        String tagProviderMethodName = field.getPresenterTagProviderMethodName();
        if (tagProviderMethodName != null) {
            classBuilder.addMethod(generateGetTagMethod(tagProviderMethodName, targetClassName));
        }

        return classBuilder.build();
    }

    private static MethodSpec generatePresenterBinderConstructor(TargetPresenterField field,
        String tag) {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super($S, $S, $T.class)",
                tag,
                field.getPresenterId(),
                field.getTypeName())
            .build();
    }

    private static MethodSpec generateBindMethod(TargetPresenterField field,
        ClassName targetClassName) {
        return MethodSpec.methodBuilder("bind")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(targetClassName, "target")
            .addParameter(MvpPresenter.class, "presenter")
            .addStatement("target.$L = ($T) presenter", field.getName(), field.getTypeName())
            .build();
    }

    private static MethodSpec generateProvidePresenterMethod(TargetPresenterField field,
        ClassName targetClassName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("providePresenter")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName
                .get(ClassName.get(MvpPresenter.class), WildcardTypeName.subtypeOf(Object.class)))
            .addParameter(targetClassName, "delegated");

        if (field.getPresenterProviderMethodName() != null) {
            builder.addStatement("return delegated.$L()", field.getPresenterProviderMethodName());
        } else {
            boolean hasEmptyConstructor = Util
                .hasEmptyConstructor((TypeElement) ((DeclaredType) field.getClazz()).asElement());

            if (hasEmptyConstructor) {
                builder.addStatement("return new $T()", field.getTypeName());
            } else {
                builder.addStatement("throw new $T($S + $S)", IllegalStateException.class, field.getClazz(),
                    " hasn't got a default constructor. You can apply @ProvidePresenter to a method which will "
                        + "construct Presenter. Also you can make it default constructor");
            }
        }

        return builder.build();
    }

    private static MethodSpec generateGetTagMethod(String tagProviderMethodName,
        ClassName targetClassName) {
        return MethodSpec.methodBuilder("getTag")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addParameter(targetClassName, "delegated")
            .addStatement("return String.valueOf(delegated.$L())", tagProviderMethodName)
            .build();
    }

    @Override
    public List<JavaFile> generate(TargetClassInfo targetClassInfo) {
        ClassName targetClassName = targetClassInfo.getName();
        List<TargetPresenterField> fields = targetClassInfo.getFields();
        TypeElement superPresenterBinder = targetClassInfo.getSuperPresenterBinder();

        final String containerSimpleName = String.join("$", targetClassName.simpleNames());

        TypeSpec.Builder classBuilder = TypeSpec
            .classBuilder(containerSimpleName + MvpProcessor.PRESENTER_BINDER_SUFFIX)
            .addModifiers(Modifier.PUBLIC)
            .superclass(
                ParameterizedTypeName.get(ClassName.get(PresenterBinder.class), targetClassName));

        for (TargetPresenterField field : fields) {
            classBuilder.addType(generatePresenterBinderClass(field, targetClassName));
        }

        classBuilder.addMethod(generateGetPresentersMethod(fields, targetClassName, superPresenterBinder));

        return Collections.singletonList(ExtensionsKt.toJavaFile(classBuilder.build(), targetClassName));
    }
}

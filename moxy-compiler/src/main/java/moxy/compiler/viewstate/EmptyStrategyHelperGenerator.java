package moxy.compiler.viewstate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;

public class EmptyStrategyHelperGenerator {


    /**
     * @param destinationPackage package to generate EmptyStrategyHelper
     * @param migrationMethods   non empty list of methods
     */
    public static JavaFile generate(String destinationPackage,
            List<MigrationMethod> migrationMethods) {

        Name firstViewSimpleName = migrationMethods.get(0).clazz.getSimpleName();

        TypeSpec.Builder classBuilder = TypeSpec
                .classBuilder("EmptyStrategyHelper")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        String javaDoc =
                String.format(
                        "This is a helper class. See the clickable list of methods witch need in refactoring.\n"
                                + "Do not pay attention to errors like:\n\n"
                                + "\'error: %s is abstract; cannot be instantiated\'\n\n"
                                + "To complete migration just open the method and set the necessary strategy to it",
                        firstViewSimpleName);
        classBuilder.addJavadoc(
                javaDoc);

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("getViewStateProviders")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        for (int i = 0; i < migrationMethods.size(); i++) {
            MigrationMethod migrationMethod = migrationMethods.get(i);
            String statement = String
                    .format("new %s().%s()", migrationMethod.clazz.getQualifiedName(),
                            migrationMethod.method.getSimpleName());
            methodSpecBuilder.addStatement(statement);
        }

        classBuilder.addMethod(methodSpecBuilder.build());

        return JavaFile.builder(destinationPackage, classBuilder.build())
                .indent("\t")
                .build();
    }
}

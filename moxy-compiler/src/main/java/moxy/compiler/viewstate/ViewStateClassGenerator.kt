package moxy.compiler.viewstate

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeSpec.Builder
import moxy.MvpProcessor
import moxy.compiler.JavaFilesGenerator
import moxy.compiler.MvpCompiler
import moxy.compiler.Util
import moxy.compiler.Util.decapitalizeString
import moxy.compiler.className
import moxy.compiler.parametrizedWith
import moxy.compiler.toJavaFile
import moxy.viewstate.MvpViewState
import moxy.viewstate.ViewCommand
import javax.lang.model.element.Modifier
import javax.lang.model.type.DeclaredType

class ViewStateClassGenerator : JavaFilesGenerator<ViewInterfaceInfo> {

    override fun generate(viewInterfaceInfo: ViewInterfaceInfo): List<JavaFile> {
        val viewName = viewInterfaceInfo.name
        val nameWithTypeVariables = viewInterfaceInfo.nameWithTypeVariables
        val viewInterfaceType = viewInterfaceInfo.element.asType() as DeclaredType

        val typeName = Util.getSimpleClassName(viewInterfaceInfo.element) + MvpProcessor.VIEW_STATE_SUFFIX
        val classBuilder: Builder = TypeSpec.classBuilder(typeName)
            .addModifiers(Modifier.PUBLIC)
            .superclass(MvpViewState::class.className().parametrizedWith(nameWithTypeVariables))
            .addSuperinterface(nameWithTypeVariables)
            .addTypeVariables(viewInterfaceInfo.typeVariables)

        for (method in viewInterfaceInfo.methods) {
            val commandClass = generateCommandClass(method, nameWithTypeVariables)
            classBuilder.addType(commandClass)
            classBuilder.addMethod(generateMethod(viewInterfaceType, method, nameWithTypeVariables, commandClass))
        }

        return listOf(classBuilder.build().toJavaFile(viewName))
    }

    private fun generateCommandClass(
        method: ViewMethod,
        viewTypeName: TypeName
    ): TypeSpec {
        val applyMethod: MethodSpec? = MethodSpec.methodBuilder("apply")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(viewTypeName, "mvpView")
            .addExceptions(method.exceptions)
            .addStatement("mvpView.$1L($2L)", method.name, method.argumentsString)
            .build()

        val classBuilder = TypeSpec.classBuilder(method.commandClassName)
            .addModifiers(Modifier.PUBLIC) // TODO: private and static
            .addTypeVariables(method.typeVariables)
            .superclass(ViewCommand::class.className().parametrizedWith(viewTypeName))
            .addMethod(generateCommandConstructor(method))
            .addMethod(applyMethod)

        for (parameter in method.parameters) {
            // TODO: private field
            classBuilder.addField(parameter.type, parameter.name, Modifier.PUBLIC, Modifier.FINAL)
        }
        return classBuilder.build()
    }

    private fun generateMethod(
        enclosingType: DeclaredType,
        method: ViewMethod,
        viewTypeName: TypeName,
        commandClass: TypeSpec
    ): MethodSpec? {
        // TODO: val commandFieldName = "$cmd";

        var commandFieldName: String = decapitalizeString(method.commandClassName)
        var iterationVariableName = "view"

        // Add salt if contains argument with same name
        while (method.argumentsString.contains(commandFieldName)) {
            commandFieldName += commandFieldName.hashCode() % 10
        }
        while (method.argumentsString.contains(iterationVariableName)) {
            iterationVariableName += iterationVariableName.hashCode() % 10
        }

        return MethodSpec.overriding(method.element, enclosingType, MvpCompiler.typeUtils)
            .addStatement("$1N $2L = new $1N($3L)", commandClass, commandFieldName, method.argumentsString)
            .addStatement("this.viewCommands.beforeApply($1L)", commandFieldName)
            .addCode("\n")
            .beginControlFlow("if (hasNotView())")
            .addStatement("return")
            .endControlFlow()
            .addCode("\n")
            .beginControlFlow("for ($1T $iterationVariableName : this.views)", viewTypeName)
            .addStatement("$iterationVariableName.$1L($2L)", method.name, method.argumentsString)
            .endControlFlow()
            .addCode("\n")
            .addStatement("this.viewCommands.afterApply($1L)", commandFieldName)
            .build()
    }

    private fun generateCommandConstructor(method: ViewMethod): MethodSpec? {
        val parameters: List<ParameterSpec> = method.parameters

        val builder: MethodSpec.Builder = MethodSpec.constructorBuilder()
            .addParameters(parameters)
            .addStatement("super($1S, $2T.class)", method.tag, method.strategy)

        if (parameters.isNotEmpty()) builder.addCode("\n")

        for (parameter in parameters) {
            builder.addStatement("this.$1N = $1N", parameter)
        }

        return builder.build()
    }
}
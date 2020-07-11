package moxy.ksp

import org.jetbrains.kotlin.ksp.processing.CodeGenerator
import org.jetbrains.kotlin.ksp.processing.Resolver
import org.jetbrains.kotlin.ksp.processing.SymbolProcessor
import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

class ViewStateProccessor : SymbolProcessor {
    lateinit var codeGenerator: CodeGenerator

    override fun finish() {

    }

    override fun init(options: Map<String, String>, kotlinVersion: KotlinVersion, codeGenerator: CodeGenerator) {
        this.codeGenerator = codeGenerator
    }

    override fun process(resolver: Resolver) {
        val fileKt = codeGenerator.createNewFile("", "HELLO", "java")
        fileKt.appendText("public class HELLO{\n")
        fileKt.appendText("public int foo() { return 1234; }\n")
        fileKt.appendText("}")
    }
}

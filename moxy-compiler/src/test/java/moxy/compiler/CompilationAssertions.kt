package moxy.compiler

import com.google.testing.compile.Compilation
import com.google.testing.compile.CompilationSubject
import org.junit.Assert


fun Compilation.assertSucceededWithoutWarnings() {
    CompilationSubject.assertThat(this).succeededWithoutWarnings()
}

fun Compilation.assertSucceeded() {
    CompilationSubject.assertThat(this).succeeded()
}

fun Compilation.assertFailed() {
    CompilationSubject.assertThat(this).failed()
}

fun Compilation.assertGeneratedSourceFileNumberIs(expectedNumber: Int) {
    Assert.assertEquals(expectedNumber, generatedSourceFiles().size)
}

fun Compilation.assertThatIt(): CompilationSubject {
    return CompilationSubject.assertThat(this)
}
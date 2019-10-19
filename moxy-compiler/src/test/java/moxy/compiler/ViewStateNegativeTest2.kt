package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class ViewStateNegativeTest2 : CompilerTest() {

    @Test
    fun errorIfInjectViewStateNotOnPresenter() {
        @Language("JAVA") val presenter = """
            import moxy.InjectViewState;
            
            @InjectViewState
            public class InjectViewStateNotOnPresenter {
        
            }
        """.toJavaFile()

        // TODO add human readable message for @InjectViewState being placed not on MvpPresenter subclass

        val compilation = compileSourcesWithProcessor(presenter)
        compilation.assertFailed()
    }
}
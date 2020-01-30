package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class GenericBasePresenter : CompilerTest() {

    @Test
    fun testBasePresenterWithTypeVariableSucceeds() {
        @Language("JAVA") val presenter = """
            import moxy.MvpView;
            import moxy.MvpPresenter;
            
            public class BasePresenter<Base extends MvpView> extends MvpPresenter<Base> { }
            """.toJavaFile()

        val compilation = compileSourcesWithProcessor(presenter)
        compilation.assertSucceededWithoutWarnings()
    }
}
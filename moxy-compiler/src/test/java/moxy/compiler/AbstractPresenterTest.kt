package moxy.compiler

import com.google.common.truth.Truth.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Test

/**
 * https://github.com/moxy-community/Moxy/issues/101
 */
class AbstractPresenterTest : CompilerTest() {

    @Test
    fun doesNotGenerateViewState() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            
            public interface MyView extends MvpView { }
        """.toJavaFile()

        @Language("JAVA") val presenter = """
            import moxy.MvpPresenter;
            
            public abstract class AbstractPresenter extends MvpPresenter<MyView> { }
        """.toJavaFile()

        val compilationResult = compileSourcesWithProcessor(presenter, view)
        assertThat(compilationResult.generatedSourceFiles()).isEmpty()
    }
}

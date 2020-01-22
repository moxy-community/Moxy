package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class ViewStateNegativeTestKt : CompilerTest() {

    //TODO: can't test it for kotlin sources
    @Test
    fun successIfInjectViewStateNotOnPresenter() {
        @Language("JAVA") val presenter = """
            import moxy.InjectViewState;
            import moxy.MvpPresenter;
            import moxy.view.TestView;
            
            public class InjectViewStateNotOnPresenter extends MvpPresenter<TestView> {
        
            }
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(presenter)
        compilation.assertSucceededWithoutWarnings()
    }

    @Test
    fun checkWithoutInjectViewState() {
        @Language("JAVA") val presenter = """
            import moxy.MvpPresenter;
            
            public class InjectViewStateNotOnPresenter extends MvpPresenter<EmptyView> {
        
            }
        """.toJavaFile()

        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(AddToEndSingleStrategy.class)
            public interface EmptyView extends MvpView {
            }

        """.toJavaFile()

        val expected = """
            import moxy.viewstate.MvpViewState;
            
            public class EmptyView${"$$"}State extends MvpViewState<EmptyView> implements EmptyView {
            }
        """.toJavaFile()

        val presenterCompilation = compileSourcesWithProcessor(presenter, view)
        val viewStateCompilation = compileSources(view, expected)

        assertExceptedFilesGenerated(
                presenterCompilation.generatedFiles(),
                viewStateCompilation.generatedFiles())
    }
}
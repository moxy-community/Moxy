package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class ViewStateNegativeTestKt : CompilerTest() {

    //TODO: can't test it for kotlin sources
    @Test
    fun successWithoutInjectViewStateOnPresenter() {
        @Language("JAVA") val presenter = """
            import moxy.InjectViewState;
            import moxy.MvpPresenter;
            import moxy.view.TestView;
            
            public class WithoutInjectViewStatePresenter extends MvpPresenter<TestView> {
        
            }
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(presenter)
        compilation.assertSucceededWithoutWarnings()
    }

    @Test
    fun checkWithoutInjectViewState() {
        @Language("JAVA") val presenter = """
            import moxy.MvpPresenter;
            
            public class WithoutInjectViewStatePresenter extends MvpPresenter<EmptyView> {
        
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

        assertExpectedFilesGenerated(
                presenterCompilation.generatedFiles(),
                viewStateCompilation.generatedFiles())
    }

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
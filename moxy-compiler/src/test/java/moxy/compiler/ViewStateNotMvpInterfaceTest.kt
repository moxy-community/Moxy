package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class ViewStateNotMvpInterfaceTest : CompilerTest() {

    companion object {
        @Language("JAVA")
        const val otherModuleInterfaceSource = """            
            public interface OtherModuleView {
                void helloFromOtherModule();
                void helloFromOtherModule(String reload);
            }
        """

        @Language("JAVA") val presenterSource = """      
            import moxy.InjectViewState;
            import moxy.MvpPresenter;

            @InjectViewState
            public class MainPresenter extends MvpPresenter<MainView> {
                @Override
                protected void onFirstViewAttach() {
                    super.onFirstViewAttach();
                    getViewState().printLog("TEST");
                }
            
                void printLog() {
                    getViewState().printLog("TEST print log " + hashCode());
                }
            
                void onOpenKtxButtonClick() {
                    getViewState().openKtxActivity();
                }
            }
        """
    }

    @Test
    fun successOtherModuleViewInterface() {
        val otherModuleInterface = otherModuleInterfaceSource.toJavaFile()
        val presenter = presenterSource.toJavaFile()
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;      
            public interface MainView extends MvpView, OtherModuleView {
                @StateStrategyType(AddToEndStrategy.class)
                void printLog(String msg);
            
                @StateStrategyType(OneExecutionStateStrategy.class)
                void openKtxActivity();
            
                @StateStrategyType(AddToEndStrategy.class)
                @Override
                void helloFromOtherModule();
            
                @StateStrategyType(AddToEndStrategy.class)
                @Override
                void helloFromOtherModule(String reload);
            }
        """.toJavaFile()
        val compilation = compileSourcesWithProcessor(otherModuleInterface, view, presenter)
        compilation.assertSucceededWithoutWarnings()
    }

    @Test
    fun successOtherModuleViewInterfaceDefaultStrategy() {
        val otherModuleInterface = otherModuleInterfaceSource.toJavaFile()
        val presenter = presenterSource.toJavaFile()
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;      
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            public interface MainView extends MvpView, OtherModuleView {
                void printLog(String msg);
            
                void openKtxActivity();
            
                @Override
                void helloFromOtherModule();
            
                @Override
                void helloFromOtherModule(String reload);
            }
        """.toJavaFile()
        val compilation = compileSourcesWithProcessor(otherModuleInterface, view, presenter)
        compilation.assertSucceededWithoutWarnings()
    }

    @Test
    fun failureOtherModuleViewInterface() {
        val otherModuleInterface = otherModuleInterfaceSource.toJavaFile()
        val presenter = presenterSource.toJavaFile()
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;      
            
            public interface MainView extends MvpView, OtherModuleView {
                @StateStrategyType(AddToEndStrategy.class)
                void printLog(String msg);
            
                @StateStrategyType(OneExecutionStateStrategy.class)
                void openKtxActivity();
            }
        """.toJavaFile()
        val compilation = compileSourcesWithProcessor(otherModuleInterface, view, presenter)
        compilation.assertFailed()
    }
}
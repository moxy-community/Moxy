package moxy.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class ViewStateNotMvpInterfaceTest : CompilerTest() {

    @Test
    fun successOtherModuleViewInterface() {
        @Language("kotlin") val sources = """
            import moxy.InjectViewState
            import moxy.MvpPresenter
            import moxy.view.TestView
            import moxy.MvpView
            import moxy.viewstate.strategy.AddToEndStrategy
            import moxy.viewstate.strategy.StateStrategyType
            import moxy.viewstate.strategy.OneExecutionStateStrategy
            
            interface OtherModuleView {
                fun helloFromOtherModule()
            }
            
            interface MainView : MvpView, OtherModuleView {
                @StateStrategyType(AddToEndStrategy::class)
                fun printLog(msg: String)
            
                @StateStrategyType(OneExecutionStateStrategy::class)
                fun openKtxActivity()
            
                @StateStrategyType(AddToEndStrategy::class)
                override fun helloFromOtherModule()
            }

            class MainPresenter: MvpPresenter<MainView>() {
            
                override fun onFirstViewAttach() {
                    super.onFirstViewAttach()
                    viewState.printLog("TEST")
                }
            
                fun printLog() {
                    viewState.printLog("TEST print log ${hashCode()}")
                }
            
                fun onOpenKtxButtonClick() {
                    viewState.openKtxActivity()
                }
            }
        """
        val result = compileKotlinSourcesWithProcessor(SourceFile.kotlin("KClass.kt", sources))
        assertEquals(result.exitCode, KotlinCompilation.ExitCode
            .OK
        )
    }

    @Test
    fun successOtherModuleViewInterfaceDefaultStrategy() {
        @Language("kotlin") val sources = """
            import moxy.InjectViewState
            import moxy.MvpPresenter
            import moxy.view.TestView
            import moxy.MvpView
            import moxy.viewstate.strategy.AddToEndStrategy
            import moxy.viewstate.strategy.StateStrategyType
            import moxy.viewstate.strategy.OneExecutionStateStrategy
            
            interface OtherModuleView {
                fun helloFromOtherModule()
            }
            
            @StateStrategyType(OneExecutionStateStrategy::class)
            interface MainView : MvpView, OtherModuleView {
                fun printLog(msg: String)
            
                fun openKtxActivity()
            
                override fun helloFromOtherModule()
            }

            class MainPresenter: MvpPresenter<MainView>() {
            
                override fun onFirstViewAttach() {
                    super.onFirstViewAttach()
                    viewState.printLog("TEST")
                }
            
                fun printLog() {
                    viewState.printLog("TEST print log ${hashCode()}")
                }
            
                fun onOpenKtxButtonClick() {
                    viewState.openKtxActivity()
                }
            }
        """
        val result = compileKotlinSourcesWithProcessor(SourceFile.kotlin("KClass.kt", sources))
        assertEquals(result.exitCode, KotlinCompilation.ExitCode
            .OK
        )
    }

    @Test
    fun failureOtherModuleViewInterface() {
        @Language("kotlin") val sources = """
            import moxy.InjectViewState
            import moxy.MvpPresenter
            import moxy.view.TestView
            import moxy.MvpView
            import moxy.viewstate.strategy.AddToEndStrategy
            import moxy.viewstate.strategy.StateStrategyType
            import moxy.viewstate.strategy.OneExecutionStateStrategy
            
            interface OtherModuleView {
                fun helloFromOtherModule()
            }
            
            interface MainView : MvpView, OtherModuleView {
                @StateStrategyType(AddToEndStrategy::class)
                fun printLog(msg: String)
            
                @StateStrategyType(OneExecutionStateStrategy::class)
                fun openKtxActivity()
            
                override fun helloFromOtherModule()
            }

            class MainPresenter: MvpPresenter<MainView>() {
            
                override fun onFirstViewAttach() {
                    super.onFirstViewAttach()
                    viewState.printLog("TEST")
                }
            
                fun printLog() {
                    viewState.printLog("TEST print log ${hashCode()}")
                }
            
                fun onOpenKtxButtonClick() {
                    viewState.openKtxActivity()
                }
            }
        """
        val result = compileKotlinSourcesWithProcessor(SourceFile.kotlin("KClass.kt", sources))
        assertEquals(result.exitCode, KotlinCompilation.ExitCode
            .COMPILATION_ERROR
        )
    }

    private fun compileKotlinSourcesWithProcessor(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return KotlinCompilation().apply {
            sources = sourceFiles.toList()
            annotationProcessors = listOf(MvpCompiler())
            messageOutputStream = System.out
            inheritClassPath = true
        }
            .compile()
    }
}
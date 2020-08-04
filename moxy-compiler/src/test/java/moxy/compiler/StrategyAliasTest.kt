package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class StrategyAliasTest : CompilerTest() {

    @Test
    fun methodWithTwoStateStrategiesWillFail() {
        @Language("JAVA") val viewInterface = """
            package moxy;
            
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            @interface OneExecution {
            }
            
            public interface ViewInterface extends MvpView {
                @OneExecution
                @StateStrategyType(AddToEndSingleStrategy.class)
                void twoStrategiesMethod(int a, String b);
            }
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(viewInterface, generateViewStateFor("moxy.ViewInterface"))

        compilation.assertThatIt()
            .hadErrorContaining("There's more than one state strategy type defined for method")
            .inFile(viewInterface)
            .onLineContaining("void twoStrategiesMethod(int a, String b)")
    }

    @Test
    fun interfaceWithTwoStateStrategiesWillFail() {
        @Language("JAVA") val viewInterface = """
            package moxy;
            
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            @interface OneExecution {
            }
            
            @OneExecution
            @StateStrategyType(AddToEndSingleStrategy.class)
            public interface ViewInterface extends MvpView {
                void twoStrategiesMethod(int a, String b);
            }
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(viewInterface, generateViewStateFor("moxy.ViewInterface"))

        compilation.assertThatIt()
            .hadErrorContaining("There's more than one state strategy type defined for 'ViewInterface'")
            .inFile(viewInterface)
            .onLineContaining("public interface ViewInterface")
    }

    @Test
    fun testAliasedStateStrategy() {
        @Language("JAVA") val viewInterface = """
            package moxy;
            
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            @interface OneExecution {
            }
            
            public interface ViewInterface extends MvpView {
                @OneExecution
                void testMethod();
            }
        """.toJavaFile()

        val expected = GENERATED_VIEW_INTERFACE_WITH_ONE_EXECUTION_TEST_METHOD

        val presenter = generateViewStateFor("moxy.ViewInterface")
        val compilation = compileSourcesWithProcessor(viewInterface, presenter)

        val expectedCompilation = compileSources(viewInterface, presenter, expected)

        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            expectedCompilation.generatedFiles())
    }

    @Test
    fun testAliasedStateStrategyOnInterface() {
        @Language("JAVA") val viewInterface = """
            package moxy;
            
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            @interface OneExecution {
            }
            
            @OneExecution
            public interface ViewInterface extends MvpView {
                void testMethod();
            }
        """.toJavaFile()

        val expected = GENERATED_VIEW_INTERFACE_WITH_ONE_EXECUTION_TEST_METHOD

        val presenter = generateViewStateFor("moxy.ViewInterface")
        val compilation = compileSourcesWithProcessor(viewInterface, presenter)

        val expectedCompilation = compileSources(viewInterface, presenter, expected)

        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            expectedCompilation.generatedFiles())
    }

    @Test
    fun testLibraryOneExecutionAliasStateStrategy() {
        @Language("JAVA") val viewInterface = """
            package moxy;
            
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.alias.OneExecution;
            
            @OneExecution
            public interface ViewInterface extends MvpView {
                void testMethod();
            }
        """.toJavaFile()

        val expected = GENERATED_VIEW_INTERFACE_WITH_ONE_EXECUTION_TEST_METHOD

        val presenter = generateViewStateFor("moxy.ViewInterface")
        val compilation = compileSourcesWithProcessor(viewInterface, presenter)

        val expectedCompilation = compileSources(viewInterface, presenter, expected)

        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            expectedCompilation.generatedFiles())
    }

    companion object {
        private val GENERATED_VIEW_INTERFACE_WITH_ONE_EXECUTION_TEST_METHOD = """
            package moxy;

            import java.lang.Override;
            import moxy.viewstate.MvpViewState;
            import moxy.viewstate.ViewCommand;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;

            public class ViewInterface${"$$"}State extends MvpViewState<ViewInterface> implements ViewInterface {
            	@Override
            	public void testMethod() {
            		TestMethodCommand testMethodCommand = new TestMethodCommand();
            		this.viewCommands.beforeApply(testMethodCommand);

            		if (hasNotView()) {
            			return;
            		}

            		for (ViewInterface view : this.views) {
            			view.testMethod();
            		}

            		this.viewCommands.afterApply(testMethodCommand);
            	}

            	public class TestMethodCommand extends ViewCommand<ViewInterface> {
            		TestMethodCommand() {
            			super("testMethod", OneExecutionStateStrategy.class);
            		}

            		@Override
            		public void apply(ViewInterface mvpView) {
            			mvpView.testMethod();
            		}
            	}
            }
        """.toJavaFile()
    }

}
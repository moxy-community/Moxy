package moxy.compiler

import org.intellij.lang.annotations.Language
import org.junit.Test

class ViewStateInheritanceTest : CompilerTest() {

    @Test
    fun superInterfaceWithoutStrategiesFails() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            interface NoStrategyViewInterface extends MvpView {
                void someFun();
            }
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            public interface ExtendNoStrategyViewInterface extends NoStrategyViewInterface {
                
                void otherFun();
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertThatIt()
            .hadErrorContaining("A View method has no strategy!")
            .inFile(view)
            .onLineContaining("void someFun()")
    }

    @Test
    fun superInterfaceWithoutStrategiesDoesNotFailWhenOverridden() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            interface NoStrategyViewInterface extends MvpView {
                void someFun();
            }
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            public interface ExtendNoStrategyViewInterface extends NoStrategyViewInterface {
                
                @Override
                void someFun();
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertSucceeded()
    }

    @Test
    fun superInterfaceStrategyDoesNotInheritedByChild() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            interface NoStrategyViewInterface extends MvpView {
                void someFun();
            }
            
            public interface ExtendNoStrategyViewInterface extends NoStrategyViewInterface {
                
                @Override
                void someFun(); // <-- fails
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertThatIt()
            .hadErrorContaining("A View method has no strategy!")
            .inFile(view)
            .onLineContaining("<-- fails")
    }

    @Test
    fun superInterfaceMethodsClashWarns() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.alias.AddToEndSingle;
            import moxy.viewstate.strategy.alias.OneExecution;
            
            interface ViewA extends MvpView {
                @OneExecution
                void someFun(String bird);
            }
            
            interface ViewB extends MvpView {
                @AddToEndSingle
                void someFun(String dog);
            }
            
            public interface ExtendBothViews extends ViewA, ViewB {
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertThatIt()
            .hadWarningContaining("Strategy clash in superinterfaces of ExtendBothViews. Interface ViewA defines someFun(java.lang.String) with strategy OneExecutionStateStrategy, but ViewB defines this method with strategy AddToEndSingleStrategy. Override this method in ExtendBothViews to choose appropriate strategy")
            .inFile(view)
    }

    @Test
    fun superInterfaceMethodsClashSameStrategySucceeds() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.alias.OneExecution;
            
            interface ViewA extends MvpView {
                @OneExecution
                void someFun(int hello);
            }
            
            interface ViewB extends MvpView {
                @OneExecution
                void someFun(int moshiMoshi);
            }
            
            public interface ExtendBothViews extends ViewA, ViewB {
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertSucceededWithoutWarnings()
    }

    @Test
    fun superInterfaceMethodsClashWithoutStrategiesFailsOnMissingStrategy() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.alias.OneExecution;
            
            interface ViewA extends MvpView {
                void someFun();
            }
            
            interface ViewB extends MvpView {
                void someFun();
            }
            
            public interface ExtendBothViews extends ViewA, ViewB {
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertThatIt()
            .hadErrorContaining("A View method has no strategy!")
            .inFile(view)
    }

    @Test
    fun superInterfaceMethodsClashButOverriddenSucceeds() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            import moxy.viewstate.strategy.alias.AddToEnd;
            import moxy.viewstate.strategy.alias.AddToEndSingle;
            
            interface ViewA extends MvpView {
                @AddToEnd
                void someFun();
            }
            
            interface ViewB extends MvpView {
                @AddToEndSingle
                void someFun();
            }
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            public interface SomeView extends ViewA, ViewB {
                
                @Override
                void someFun();
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertThatIt()
            .hadWarningContaining("Strategy clash in superinterfaces of SomeView")
            .inFile(view)
    }
}
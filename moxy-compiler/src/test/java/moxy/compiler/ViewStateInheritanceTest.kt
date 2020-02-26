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
    fun superInterfaceWithoutStrategiesFailsEvenWithOverride() {
        @Language("JAVA") val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            interface NoStrategyViewInterface extends MvpView {
                void someFun(); // <-- fails
            }
            
            @StateStrategyType(OneExecutionStateStrategy.class)
            public interface ExtendNoStrategyViewInterface extends NoStrategyViewInterface {
                
                @Override
                void someFun();
            } 
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))

        compilation.assertThatIt()
            .hadErrorContaining("A View method has no strategy!")
            .inFile(view)
            .onLineContaining("<-- fails")
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
}
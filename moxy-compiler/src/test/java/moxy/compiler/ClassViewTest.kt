package moxy.compiler

import com.google.testing.compile.JavaFileObjects
import org.junit.Test

class ClassViewTest : CompilerTest() {

    @Test
    fun errorWhenViewStateGeneratedForClass() {
        val viewClassName = "ClassImplementingView"

        val view = JavaFileObjects.forSourceString(
            viewClassName,
            """
            import moxy.viewstate.strategy.OneExecutionStateStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            public class $viewClassName implements TestView {
            
                @StateStrategyType(OneExecutionStateStrategy.class)
                @Override
                public void testEvent() {
                }
            }
            """.trimIndent())

        val presenter = generateViewStateFor(viewClassName)


        val compilation = compileSourcesWithProcessor(view, presenter)
        compilation.assertFailed()
        compilation.assertThatIt()
            .hadErrorContaining("$viewClassName must be INTERFACE")
            .inFile(view)
            .onLineContaining("class $viewClassName")
    }
}
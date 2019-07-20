package moxy.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;
import static org.junit.Assert.assertEquals;

public class NestedViewTest extends CompilerTest {
    @Test
    public void testNestedViewCompilesWithoutErrors() throws Exception {
        JavaFileObject[] sources = {
                forResource("nestedview/MyContract.java"),
                forResource("nestedview/MyPresenter.java")
        };

        Compilation compilation = compileLibSourcesWithProcessor(sources);
        assertThat(compilation).succeeded();
        assertEquals(2, compilation.generatedSourceFiles().size());
    }
}

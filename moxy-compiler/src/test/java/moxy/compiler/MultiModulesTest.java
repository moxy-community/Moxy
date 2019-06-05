package moxy.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;

public class MultiModulesTest extends CompilerTest {

    @Test
    public void testLibraryModule() throws Exception {
        JavaFileObject[] sources = {
                forResource("multimodules/lib1/Lib1Presenter.java"),
                forResource("multimodules/lib1/Lib1View.java")
        };

        JavaFileObject[] generatedSources = {
                forResource("multimodules/lib1/Lib1Presenter$$ViewStateProvider.java"),
                forResource("multimodules/lib1/Lib1View$$State.java"),
        };

        Compilation compilation = compileLibSourcesWithProcessor(sources);
        Compilation exceptedCompilation = compileSources(generatedSources);

        assertThat(compilation).succeededWithoutWarnings();
        assertExceptedFilesGenerated(compilation.generatedFiles(), exceptedCompilation.generatedFiles());
    }
}

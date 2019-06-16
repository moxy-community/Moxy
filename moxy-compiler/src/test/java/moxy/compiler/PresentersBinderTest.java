package moxy.compiler;

import com.google.testing.compile.Compilation;
import javax.tools.JavaFileObject;
import moxy.MvpProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.google.testing.compile.CompilationSubject.assertThat;

@RunWith(Parameterized.class)
public class PresentersBinderTest extends CompilerTest {

    @Parameterized.Parameter
    public String targetClassName;

    @Parameterized.Parameters(name = "{0}")
    public static String[] data() {
        return new String[] {
            "target.SimpleInjectPresenterTarget",
            "target.SimpleProvidePresenterTarget",
            "target.GenericPresenterTarget",
        };
    }

    @Test
    public void test() throws Exception {
        JavaFileObject target = getSourceFile(targetClassName);
        JavaFileObject exceptedPresentersBinder =
            getSourceFile(targetClassName + MvpProcessor.PRESENTER_BINDER_SUFFIX);

        Compilation targetCompilation = compileSourcesWithProcessor(target);
        Compilation exceptedPresentersBinderCompilation = compileSources(exceptedPresentersBinder);

        assertThat(targetCompilation).succeededWithoutWarnings();
        assertExceptedFilesGenerated(targetCompilation.generatedFiles(),
            exceptedPresentersBinderCompilation.generatedFiles());
    }
}

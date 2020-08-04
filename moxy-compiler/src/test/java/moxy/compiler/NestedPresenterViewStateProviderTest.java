package moxy.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.tools.JavaFileObject;

import moxy.MvpProcessor;

import static com.google.testing.compile.CompilationSubject.assertThat;

@RunWith(Parameterized.class)
public class NestedPresenterViewStateProviderTest extends CompilerTest {

    @Parameterized.Parameter
    public TestParams testParams;

    @Parameterized.Parameters(name = "{0}")
    public static TestParams[] data() {
        return new TestParams[] {
            new TestParams(
                "presenter.PresenterWrapper",
                "presenter.PresenterWrapper$EmptyViewPresenter" + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX
            ),
        };
    }

    @Test
    public void test() throws Exception {
        JavaFileObject presenter = getSourceFile(testParams.sourceFileName);
        JavaFileObject expectedViewStateProvider = getSourceFile(testParams.compiledFileName);

        Compilation presenterCompilation = compileSourcesWithProcessor(presenter);
        Compilation expectedViewStateProviderCompilation =
            compileSources(expectedViewStateProvider);

        assertThat(presenterCompilation).succeeded();
        assertExpectedFilesGenerated(presenterCompilation.generatedFiles(),
            expectedViewStateProviderCompilation.generatedFiles());
    }

    private static class TestParams {
        final String sourceFileName;
        final String compiledFileName;

        TestParams(String sourceFileName, String compiledFileName) {
            this.sourceFileName = sourceFileName;
            this.compiledFileName = compiledFileName;
        }
    }
}

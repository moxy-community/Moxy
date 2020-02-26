package moxy.compiler

import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.JavaFileObjects
import org.junit.Test

class ParameterNameClashTest : CompilerTest() {

    // If View interface has methods with parameter names clashing with ones used in generated code,
    // such as 'view', 'views' or 'viewCommands', compilation should not fail

    @Test
    fun test() {
        val viewClass = "view.parameternameclash.ParameterNameClashView"
        val dummyPresenter = JavaFileObjects.forSourceLines(
            "presenter.DummyPresenter",
            "package presenter;",
            "@moxy.InjectViewState",
            "public class DummyPresenter extends moxy.MvpPresenter<$viewClass> {}"
        )

        val presenterCompilation = compileSourcesWithProcessor(getSourceFile(viewClass), dummyPresenter)
        assertThat(presenterCompilation).succeededWithoutWarnings()
    }
}

package io.moxy.compiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;

import static org.junit.Assert.fail;

public class ViewStateClassTest extends CompilerTest {

    @Test
    public void viewStateForClassView_throw() {
        try {
            getThat(JavaFileObjects.forResource("view/CounterTestView.java"),
                    JavaFileObjects.forResource("presenter/InjectViewStateForClassPresenter.java")).failsToCompile();
            fail();
        } catch (RuntimeException e) {
            Truth.assertThat(e.getLocalizedMessage().contains("must be INTERFACE, or not mark it as"));
        }
    }

    @Test
    public void positiveViewState() {
        try {
            assertCompilationResultIs(ImmutableTable.<Diagnostic.Kind, Integer, Pattern>of(),
                    ImmutableList.of(getString(
                            "io/moxy/view/PositiveViewStateView$$State.java")));
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        }
    }
}

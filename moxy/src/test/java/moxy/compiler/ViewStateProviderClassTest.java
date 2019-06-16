package moxy.compiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.tools.Diagnostic;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ViewStateProviderClassTest extends CompilerTest {

    @Test
    public void positiveViewStateProvider() {
        try {
            assertCompilationResultIs(ImmutableTable.<Diagnostic.Kind, Integer, Pattern>of(), ImmutableList
                    .of(getString("presenter/PositiveViewStateProviderPresenter$$ViewStateProvider.java")));
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        }
    }
}

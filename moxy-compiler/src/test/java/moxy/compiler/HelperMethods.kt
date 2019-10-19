package moxy.compiler

import com.google.testing.compile.JavaFileObjects
import javax.tools.JavaFileObject

fun generateViewStateFor(view: String): JavaFileObject {
    return JavaFileObjects.forSourceString(
        "InjectViewStateForViewPresenter",
        """
        package presenter;

        import moxy.InjectViewState;
        import moxy.MvpPresenter;
        
        @InjectViewState
        public class InjectViewStateForViewPresenter extends MvpPresenter<$view> {
        
        }
        """.trimIndent())
}
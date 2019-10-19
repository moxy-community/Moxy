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

fun String.toJavaFile(fullyQualifiedName: String): JavaFileObject {
    return JavaFileObjects.forSourceString(fullyQualifiedName, this)
}

/**
 * Infers file name from first identifier after "public class" or "public interface"
 */
fun String.toJavaFile(): JavaFileObject {
    val fullyQualifiedName = if (contains("public class ")) {
        lines().first { it.contains("public class ") }
            .substringAfter("public class ")
            .takeWhile { it.isLetterOrDigit() }
    } else {
        lines().first { it.contains("public interface ") }
            .substringAfter("public interface ")
            .takeWhile { it.isLetterOrDigit() }
    }
    return toJavaFile(fullyQualifiedName)
}
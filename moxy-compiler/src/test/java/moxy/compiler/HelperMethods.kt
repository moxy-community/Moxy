package moxy.compiler

import com.google.testing.compile.JavaFileObjects
import javax.tools.JavaFileObject

fun generateViewStateFor(view: String): JavaFileObject {
    return JavaFileObjects.forSourceString(
        "InjectViewStateForViewPresenter",
        """
        import moxy.InjectViewState;
        import moxy.MvpPresenter;
        
        @InjectViewState
        public class InjectViewStateForViewPresenter extends MvpPresenter<$view> {
        
        }
        """.trimIndent()
    )
}

fun String.toJavaFile(fullyQualifiedName: String): JavaFileObject {
    return JavaFileObjects.forSourceString(fullyQualifiedName, this)
}

/**
 * Infers file name from first identifier after "public class" or "public interface"
 */
fun String.toJavaFile(): JavaFileObject {
    val publicClassPrefix = "public class "
    val publicAbstractClassPrefix = "public abstract class "
    val publicInterfacePrefix = "public interface "
    val fullyQualifiedName = when {
        contains(publicClassPrefix) -> {
            parseClassNameAfterPrefix(this, publicClassPrefix)
        }
        contains(publicAbstractClassPrefix) -> {
            parseClassNameAfterPrefix(this, publicAbstractClassPrefix)
        }
        contains(publicInterfacePrefix) -> {
            parseClassNameAfterPrefix(this, publicInterfacePrefix)
        }
        else -> throw IllegalArgumentException("Can't infer class name from the source code")
    }
    return toJavaFile(fullyQualifiedName)
}

private fun parseClassNameAfterPrefix(
    sourceCode: String,
    prefix: String
): String {
    return sourceCode.lines()
        .first { it.contains(prefix) }
        .substringAfter(prefix)
        .takeWhile { it.isLetterOrDigit() || it == '$' }
}

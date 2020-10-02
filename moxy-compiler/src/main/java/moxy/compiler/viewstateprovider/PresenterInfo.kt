package moxy.compiler.viewstateprovider

import com.squareup.javapoet.ClassName
import javax.lang.model.element.TypeElement

/**
 * Represents presenter class annotated with `@InjectViewState`.
 * `ViewStateProvider` will be generated based on data from this class.
 */
class PresenterInfo(val element: TypeElement, viewStateName: String) {
    val name: ClassName = ClassName.get(element)
    val viewStateName: ClassName = ClassName.bestGuess(viewStateName)
}
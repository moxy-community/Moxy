package moxy.compiler.viewstateprovider

import com.squareup.javapoet.ClassName
import javax.lang.model.element.TypeElement

class PresenterInfo(name: TypeElement, viewStateName: String) {
    val name: ClassName = ClassName.get(name)
    val viewStateName: ClassName = ClassName.bestGuess(viewStateName)
}
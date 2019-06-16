package moxy.compiler.viewstate;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

class MigrationMethod {

    TypeElement clazz;

    ExecutableElement method;

    MigrationMethod(final TypeElement clazz,
        final ExecutableElement method) {
        this.clazz = clazz;
        this.method = method;
    }
}
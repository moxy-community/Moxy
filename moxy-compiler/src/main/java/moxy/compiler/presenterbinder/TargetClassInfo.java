package moxy.compiler.presenterbinder;

import com.squareup.javapoet.ClassName;
import java.util.List;
import javax.lang.model.element.TypeElement;

class TargetClassInfo {

    private final ClassName name;

    private final List<TargetPresenterField> fields;

    private final TypeElement superPresenterBinder;

    TargetClassInfo(TypeElement name, List<TargetPresenterField> fields, TypeElement superPresenterBinder) {
        this.name = ClassName.get(name);
        this.fields = fields;
        this.superPresenterBinder = superPresenterBinder;
    }

    ClassName getName() {
        return name;
    }

    List<TargetPresenterField> getFields() {
        return fields;
    }

    public TypeElement getSuperPresenterBinder() {
        return superPresenterBinder;
    }
}

package moxy.compiler;

import javax.lang.model.element.Element;

public abstract class ElementProcessor<E extends Element, R> {

  public abstract R process(E element);
}

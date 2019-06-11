package moxy.compiler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

public abstract class AnnotationRule {

  protected final ElementKind validKind;

  protected final Set<Modifier> validModifiers;

  protected StringBuilder errorBuilder;

  public AnnotationRule(ElementKind validKind, Modifier... validModifiers) {
    if (validModifiers == null || validModifiers.length == 0) {
      throw new RuntimeException("Valid modifiers cant be empty or null.");
    }

    this.validKind = validKind;
    this.validModifiers = new HashSet<>(Arrays.asList(validModifiers));
    errorBuilder = new StringBuilder();
  }

  /**
   * Method describe rules for using Annotation.
   *
   * @param annotatedField Checking annotated field.
   */
  public abstract void checkAnnotation(Element annotatedField);

  public String getErrorStack() {
    return errorBuilder.toString();
  }

  protected String validModifiersToString() {
    if (validModifiers.size() > 1) {
      StringBuilder result = new StringBuilder("one of [");
      boolean addSeparator = false;
      for (Modifier validModifier : validModifiers) {
        if (addSeparator) {
          result.append(", ");
        }
        addSeparator = true;
        result.append(validModifier.toString());
      }
      result.append("]");
      return result.toString();
    } else {
      return validModifiers.iterator().next() + ".";
    }
  }
}

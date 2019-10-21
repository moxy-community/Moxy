package moxy.compiler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Element;

public interface ElementProcessor<E extends Element, R> {
    @Nullable
    R process(@NotNull E element);
}

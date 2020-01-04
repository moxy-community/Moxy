package moxy.viewstate.strategy.alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * State strategy alias for {@link AddToEndSingleStrategy}.
 * Applying this annotation has the same effect as applying {@code @StateStrategyType(AddToEndSingleStrategy.class)}.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@StateStrategyType(AddToEndSingleStrategy.class)
public @interface AddToEndSingle {
}

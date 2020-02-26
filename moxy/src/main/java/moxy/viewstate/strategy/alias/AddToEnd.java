package moxy.viewstate.strategy.alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * State strategy alias for {@link AddToEndStrategy}.
 * Applying this annotation has the same effect as applying {@code @StateStrategyType(AddToEndStrategy.class)}.
 * <br><br>
 * With this strategy command will be added to the end of the commands queue.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@StateStrategyType(AddToEndStrategy.class)
public @interface AddToEnd {
}

package moxy.viewstate.strategy.alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * State strategy alias for {@link SkipStrategy}.
 * Applying this annotation has the same effect as applying {@code @StateStrategyType(SkipStrategy.class)}.
 * <br><br>
 * With this strategy command will not be put in the commands queue
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@StateStrategyType(SkipStrategy.class)
public @interface Skip {
}

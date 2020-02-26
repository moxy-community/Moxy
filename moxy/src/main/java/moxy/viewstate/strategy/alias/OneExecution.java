package moxy.viewstate.strategy.alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * State strategy alias for {@link OneExecutionStateStrategy}.
 * Applying this annotation has the same effect as applying {@code @StateStrategyType(OneExecutionStateStrategy.class)}.
 * <br><br>
 * With this strategy command will be saved in the commands queue, but will be removed after its first execution.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@StateStrategyType(OneExecutionStateStrategy.class)
public @interface OneExecution {
}

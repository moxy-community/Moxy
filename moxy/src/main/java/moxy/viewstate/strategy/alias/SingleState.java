package moxy.viewstate.strategy.alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * State strategy alias for {@link SingleStateStrategy}.
 * Applying this annotation has the same effect as applying {@code @StateStrategyType(SingleStateStrategy.class)}.
 * <br><br>
 * This strategy will clear current commands queue and then the given command will be put in.
 * <p>
 * Caution! Be sure that you fully set view to initial state inside this command.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@StateStrategyType(SingleStateStrategy.class)
public @interface SingleState {
}

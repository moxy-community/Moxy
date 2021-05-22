package moxy.viewstate.strategy.alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import moxy.viewstate.strategy.StateStrategyTypeTag;

/**
 * State strategy alias for {@link moxy.viewstate.strategy.AddToEndSingleTagStrategy}.
 * Applying this annotation has the same effect as applying
 * {@code @StateStrategyType(AddToEndSingleTagStrategy.class, tag = "someTag")}.
 * <br><br>
 * Command will be added to the end of the commands queue. If the commands queue contains the same tag, then
 * an existing command will be removed.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@StateStrategyType(AddToEndSingleStrategy.class)
public @interface AddToEndSingleTag {

    @StateStrategyTypeTag
    String tag() default "";
}

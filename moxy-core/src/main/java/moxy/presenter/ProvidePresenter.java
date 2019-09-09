package moxy.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import moxy.PresenterStore;

/**
 * Called when Moxy can't find the right presenter instance in the {@link PresenterStore}.
 * <p>Attention! <b>Don't use manually the method marked with this annotation!</b>
 * Use a presenter field whichever you want. If you override this method in a derived class,
 * make it return the same type (not a requirement but recommendation).</p>
 * <p>Requirements:</p>
 * <ul>
 * <li>Method should return strictly the same class as the presenter field type</li>
 * <li>Presenter Types should be the same</li>
 * <li>Tags should be equal</li>
 * <li>Presenter IDs should be equal</li>
 * </ul>
 * <p>Note: if this method stays unused after the build, then Moxy'll never use this method and
 * you should check annotation parameters. These parameters should be equal to @InjectPresenter parameters</p>
 * <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidePresenter {

    String EMPTY = "";

    String tag() default EMPTY;

    String presenterId() default EMPTY;
}

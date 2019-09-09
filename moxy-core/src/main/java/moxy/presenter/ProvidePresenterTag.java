package moxy.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import moxy.MvpPresenter;
import moxy.PresenterStore;

/**
 * <p>Called when Moxy generate a presenter tag for search Presenter in {@link PresenterStore}.</p>
 * <p>Requirements:</p>
 * <ul>
 * <li>presenterClass parameter should be equal with presenter field type</li>
 * <li>Presenter Types should be the same</li>
 * <li>Presenter IDs should be equal</li>
 * </ul>
 * <p>Note: if this method stays unused after the build, then Moxy'll never use this method and you should check annotation
 * parameters.</p>
 * <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidePresenterTag {

    String EMPTY = "";

    Class<? extends MvpPresenter<?>> presenterClass();

    String presenterId() default EMPTY;
}

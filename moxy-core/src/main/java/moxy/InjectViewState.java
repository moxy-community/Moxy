package moxy;

import java.lang.annotation.Target;
import moxy.viewstate.MvpViewState;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Inject view state to {@link MvpPresenter#views} and
 * {@link MvpPresenter#viewState} presenter fields. Presenter annotated with
 * this should be strongly typed with a View interface (do not write something like extends
 * MvpPresenter&lt;V extends SuperView&gt;, or the code generation would make
 * the code that would break your app).
 */
@Target(value = TYPE)
public @interface InjectViewState {

    Class<? extends MvpViewState> value() default DefaultViewState.class;

    Class<? extends MvpView> view() default DefaultView.class;
}

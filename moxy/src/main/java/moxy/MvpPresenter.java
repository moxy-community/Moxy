package moxy;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import moxy.locators.ViewStateLocator;
import moxy.viewstate.MvpViewState;

public abstract class MvpPresenter<View extends MvpView> {

    private boolean isFirstLaunch = true;

    private String tag;

    private Set<View> views;

    private View viewStateAsView;

    private MvpViewState<View> viewState;

    private Class<? extends MvpPresenter> presenterClass;

    public MvpPresenter() {
        Binder.bind(this);

        views = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
    }

    /**
     * <p>Attach view to view state or to presenter(if view state not exists).</p>
     * <p>If you use {@link moxy.MvpDelegate}, you should not call this method directly.
     * It will be called on {@link moxy.MvpDelegate#onAttach()}, if view does not attached.</p>
     *
     * @param view to attachment
     */
    public void attachView(View view) {
        if (viewState != null) {
            viewState.attachView(view);
        } else {
            views.add(view);
        }
        if (isFirstLaunch) {
            isFirstLaunch = false;

            onFirstViewAttach();
        }
    }

    /**
     * <p>Callback after first presenter init and view binding. If this
     * presenter instance will have to attach some view in future, this method
     * will not be called.</p>
     * <p>There you can to interact with {@link #viewState}.</p>
     */
    protected void onFirstViewAttach() {
    }

    /**
     * <p>Detach view from view state or from presenter(if view state not exists).</p>
     * <p>If you use {@link moxy.MvpDelegate}, you should not call this method directly.
     * It will be called on {@link MvpDelegate#onDetach()}.</p>
     *
     * @param view view to detach
     */
    @SuppressWarnings("WeakerAccess")
    public void detachView(View view) {
        if (viewState != null) {
            viewState.detachView(view);
        } else {
            views.remove(view);
        }
    }

    public void destroyView(View view) {
        if (viewState != null) {
            viewState.destroyView(view);
        }
    }

    /**
     * @return views attached to view state, or attached to presenter(if view state not exists)
     */
    @SuppressWarnings("WeakerAccess")
    public Set<View> getAttachedViews() {
        if (viewState != null) {
            return viewState.getViews();
        }

        return views;
    }

    /**
     * @return view state, casted to view interface for simplify
     */
    @SuppressWarnings("WeakerAccess")
    public View getViewState() {
        return viewStateAsView;
    }

    /**
     * Set view state to presenter
     *
     * @param viewState that implements type, setted as View generic param
     */
    @SuppressWarnings({ "unchecked", "unused" })
    public void setViewState(MvpViewState<View> viewState) {
        viewStateAsView = (View) viewState;
        this.viewState = viewState;
    }

    /**
     * Check if view is in restore state or not
     *
     * @param view view for check
     * @return true if view state restore state to incoming view. false otherwise.
     */
    @SuppressWarnings("unused")
    public boolean isInRestoreState(View view) {
        //noinspection SimplifiableIfStatement
        if (viewState != null) {
            return viewState.isInRestoreState(view);
        }
        return false;
    }

    String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    Class<? extends MvpPresenter> getPresenterClass() {
        return presenterClass;
    }

    void setPresenterClass(Class<? extends MvpPresenter> presenterClass) {
        this.presenterClass = presenterClass;
    }

    /**
     * <p>Called before reference on this presenter will be cleared and instance of presenter
     * will be never used.</p>
     */
    public void onDestroy() {
    }

    private static class Binder {

        static void bind(MvpPresenter presenter) {
            MvpViewState viewState = ViewStateLocator.getViewState(presenter.getClass());

            presenter.viewStateAsView = (MvpView) viewState;
            presenter.viewState = viewState;
        }
    }
}

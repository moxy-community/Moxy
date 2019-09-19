package moxy.viewstate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import moxy.MvpView;
import moxy.viewstate.strategy.StateStrategy;

@SuppressWarnings("WeakerAccess")
public abstract class MvpViewState<View extends MvpView> {

    protected ViewCommands<View> viewCommands = new ViewCommands<>();

    protected Set<View> views;

    protected Set<View> inRestoreState;

    protected Map<View, Set<ViewCommand<View>>> viewStates;

    public MvpViewState() {
        views = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
        inRestoreState = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
        viewStates = new WeakHashMap<>();
    }

    /**
     * Apply a saved state to the attached view
     *
     * @param view mvp view to restore state
     * @param currentState commands that was applied already
     */
    protected void restoreState(View view, Set<ViewCommand<View>> currentState) {
        if (viewCommands.isEmpty()) {
            return;
        }

        viewCommands.reapply(view, currentState);
    }

    /**
     * @return true if the view state has one or more views, false otherwise (if the view state doesn't have any view)
     */
    protected Boolean hasNotView() {
        return (views == null) || views.isEmpty();
    }

    /**
     * Attach view to view state and apply a saved state
     *
     * @param view attachment
     */
    public void attachView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("Mvp view must be not null");
        }

        boolean isViewAdded = views.add(view);

        if (!isViewAdded) {
            return;
        }

        inRestoreState.add(view);

        Set<ViewCommand<View>> currentState = viewStates.get(view);
        currentState = currentState == null ? Collections.<ViewCommand<View>>emptySet() : currentState;

        restoreState(view, currentState);

        viewStates.remove(view);

        inRestoreState.remove(view);
    }

    /**
     * <p>Detach a view from the view state. After this call view state will save
     * commands via {@link StateStrategy#beforeApply(List, ViewCommand)}.</p>
     *
     * @param view target mvp view to detach
     */
    public void detachView(View view) {
        views.remove(view);
        inRestoreState.remove(view);

        Set<ViewCommand<View>> currentState = Collections.newSetFromMap(new WeakHashMap<ViewCommand<View>, Boolean>());
        currentState.addAll(viewCommands.getCurrentState());
        viewStates.put(view, currentState);
    }

    public void destroyView(View view) {
        viewStates.remove(view);
    }

    /**
     * @return views attached to this view state instance
     */
    public Set<View> getViews() {
        return views;
    }

    /**
     * Check if the view is in a restore state or not
     *
     * @param view a view for check
     * @return true if this view state is restoring state to the given view. false otherwise.
     */
    public boolean isInRestoreState(View view) {
        return inRestoreState.contains(view);
    }
}

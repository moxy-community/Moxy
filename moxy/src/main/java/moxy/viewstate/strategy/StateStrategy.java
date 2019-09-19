package moxy.viewstate.strategy;

import java.util.List;
import moxy.MvpView;
import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;

/**
 * Cautions:
 * <ul>
 * <li>Don't rearrange current state</li>
 * <li>Don't insert commands inside existing current state - only put them to the end of it</li>
 * <li>Be careful when you remove commands by another type. If you do it, be sure that inside your view method
 * you've fully overriden the view changes</li>
 * </ul>
 */
public interface StateStrategy {

    /**
     * Called immediately after
     * {@link MvpViewState} receives some
     * command. Will not be called before re-apply to some other
     * {@link MvpView}
     *
     * @param currentState current state of {@link MvpViewState}. Each {@link ViewCommand} contains its own parameters.
     * @param incomingCommand command for applying to a {@link MvpView} This {@link ViewCommand} contains params
     * of this command.
     * @param <View> type of the given view
     */
    <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand);

    /**
     * Called immediately after a command was applied to an {@link MvpView}. Also called
     * after re-apply to other views.
     *
     * @param currentState current state of {@link MvpViewState}. Each {@link ViewCommand} contains its own parameters.
     * @param incomingCommand applied command to {@link MvpView} This {@link ViewCommand} contains params of this
     * command.
     * @param <View> type of the given view
     */
    <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand);
}

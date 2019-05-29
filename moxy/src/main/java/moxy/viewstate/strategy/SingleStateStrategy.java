package moxy.viewstate.strategy;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.ViewCommand;

/**
 * This strategy will clear current commands queue and then incoming command will be put in.
 * <p>
 * Caution! Be sure that you fully set view to initial state inside this command.
 */
public class SingleStateStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState,
            ViewCommand<View> incomingCommand) {
        currentState.clear();
        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState,
            ViewCommand<View> incomingCommand) {
        // pass
    }
}

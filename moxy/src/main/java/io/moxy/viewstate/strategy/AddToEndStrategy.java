package io.moxy.viewstate.strategy;

import java.util.List;

import io.moxy.MvpView;
import io.moxy.viewstate.ViewCommand;

/**
 * Command will be added to end of commands queue.
 * <p>
 * This strategy used by default.
 */
public class AddToEndStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState,
            ViewCommand<View> incomingCommand) {
        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState,
            ViewCommand<View> incomingCommand) {
        // pass
    }
}

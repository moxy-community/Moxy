package moxy.viewstate.strategy;

import java.util.List;
import moxy.MvpView;
import moxy.viewstate.ViewCommand;

/**
 * Command will be added to end of commands queue.
 * <p>
 * This strategy used by default.
 */
public class AddToEndStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(final List<ViewCommand<View>> currentState,
        final ViewCommand<View> incomingCommand) {
        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(final List<ViewCommand<View>> currentState,
        final ViewCommand<View> incomingCommand) {
        // pass
    }
}

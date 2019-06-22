package moxy.viewstate.strategy;

import java.util.List;
import moxy.MvpView;
import moxy.viewstate.ViewCommand;

/**
 * Command will not be put in commands queue
 */
public class SkipStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(final List<ViewCommand<View>> currentState,
        final ViewCommand<View> incomingCommand) {
        //do nothing to skip
    }

    @Override
    public <View extends MvpView> void afterApply(final List<ViewCommand<View>> currentState,
        final ViewCommand<View> incomingCommand) {
        // pass
    }
}

package moxy.viewstate.strategy;

import java.util.Iterator;
import java.util.List;
import moxy.MvpView;
import moxy.viewstate.ViewCommand;

/**
 * Command will be added to the end of the commands queue. If commands queue contains a command of the same type,
 * then existing command will be removed.
 */
public class AddToEndSingleStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(final List<ViewCommand<View>> currentState,
        final ViewCommand<View> incomingCommand) {
        Iterator<ViewCommand<View>> iterator = currentState.iterator();

        while (iterator.hasNext()) {
            ViewCommand<View> entry = iterator.next();

            if (entry.getClass() == incomingCommand.getClass()) {
                iterator.remove();
                break;
            }
        }

        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(final List<ViewCommand<View>> currentState,
        final ViewCommand<View> incomingCommand) {
        // pass
    }
}

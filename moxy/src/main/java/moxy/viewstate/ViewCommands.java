package moxy.viewstate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import moxy.MvpView;
import moxy.locators.StrategyLocator;
import moxy.viewstate.strategy.StateStrategy;

@SuppressWarnings({ "unused", "WeakerAccess" })
public class ViewCommands<View extends MvpView> {

    private List<ViewCommand<View>> state = new ArrayList<>();

    private Map<Class<? extends StateStrategy>, StateStrategy> strategies = new HashMap<>();

    public void beforeApply(ViewCommand<View> viewCommand) {
        StateStrategy stateStrategy = getStateStrategy(viewCommand);

        stateStrategy.beforeApply(state, viewCommand);
    }

    public void afterApply(ViewCommand<View> viewCommand) {
        StateStrategy stateStrategy = getStateStrategy(viewCommand);

        stateStrategy.afterApply(state, viewCommand);
    }

    private StateStrategy getStateStrategy(ViewCommand<View> viewCommand) {
        StateStrategy stateStrategy = StrategyLocator.getStrategy(viewCommand.getStrategyType());
        if (stateStrategy == null) {
            //noinspection TryWithIdenticalCatches
            try {
                stateStrategy = viewCommand.getStrategyType().newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("Unable to create state strategy: " + viewCommand.toString());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Unable to create state strategy: " + viewCommand.toString());
            }

            strategies.put(viewCommand.getStrategyType(), stateStrategy);
        }

        return stateStrategy;
    }

    public boolean isEmpty() {
        return state.isEmpty();
    }

    public void reapply(View view, Set<ViewCommand<View>> currentState) {
        final ArrayList<ViewCommand<View>> commands = new ArrayList<>(state);

        for (ViewCommand<View> command : commands) {
            if (currentState.contains(command)) {
                continue;
            }

            command.apply(view);

            afterApply(command);
        }
    }

    public List<ViewCommand<View>> getCurrentState() {
        return state;
    }
}

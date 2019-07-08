package moxy.locators;

import java.util.HashMap;
import java.util.Map;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategy;

public class StrategyLocator {

    private static Map<Class, StateStrategy> strategies;

    static {
        strategies = new HashMap<>();
        strategies.put(AddToEndSingleStrategy.class, new AddToEndSingleStrategy());
        strategies.put(AddToEndStrategy.class, new AddToEndStrategy());
        strategies.put(OneExecutionStateStrategy.class, new OneExecutionStateStrategy());
    }

    private StrategyLocator() {
    }

    public static StateStrategy getStrategy(Class strategyClass) {
        try {
            StateStrategy stateStrategy = strategies.get(strategyClass);
            if (stateStrategy != null) return stateStrategy;
            return (StateStrategy) strategyClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + strategyClass.getName());
        }
    }
}

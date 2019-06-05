package moxy.locators;

import moxy.MoxyReflector;
import moxy.viewstate.strategy.StateStrategy;

public class StrategyLocator {

    private StrategyLocator() {
    }

    public static StateStrategy getStrategy(Class strategyClass) {
        return (StateStrategy) MoxyReflector.getStrategy(strategyClass);
    }
}

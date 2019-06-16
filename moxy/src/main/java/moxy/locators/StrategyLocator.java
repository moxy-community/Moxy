package moxy.locators;

import java.util.HashMap;
import java.util.Map;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategy;

public class StrategyLocator {

  private static Map<Class, StateStrategy> sStrategies;

  static {
    sStrategies = new HashMap<>();
    sStrategies.put(AddToEndSingleStrategy.class, new AddToEndSingleStrategy());
    sStrategies.put(AddToEndStrategy.class, new AddToEndStrategy());
    sStrategies.put(OneExecutionStateStrategy.class, new OneExecutionStateStrategy());
  }

  private StrategyLocator() {
  }

  public static StateStrategy getStrategy(Class strategyClass) {
    try {
      StateStrategy stateStrategy = sStrategies.get(strategyClass);
      if (stateStrategy != null) return stateStrategy;
      return (StateStrategy) strategyClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Cannot instantiate " + strategyClass.getName());
    }
  }
}

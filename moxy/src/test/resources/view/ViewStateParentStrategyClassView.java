package view;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ViewStateParentStrategyClassView {

  @StateStrategyType(SingleStateStrategy.class)
  void method1();

  @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test")
  void method2();
}

package view;

import io.moxy.viewstate.strategy.AddToEndSingleStrategy;
import io.moxy.viewstate.strategy.SingleStateStrategy;
import io.moxy.viewstate.strategy.StateStrategyType;

public interface ViewStateParentStrategyClassView {

    @StateStrategyType(SingleStateStrategy.class)
    void method1();

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test")
    void method2();
}

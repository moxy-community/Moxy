package view;

import io.moxy.viewstate.strategy.AddToEndSingleStrategy;
import io.moxy.viewstate.strategy.SingleStateStrategy;
import io.moxy.viewstate.strategy.StateStrategyType;

public interface ViewStateParent2View {

    @StateStrategyType(SingleStateStrategy.class)
    void method1();

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test2")
    void method2();
}

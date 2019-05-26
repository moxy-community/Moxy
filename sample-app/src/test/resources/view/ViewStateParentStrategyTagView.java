package view;

import io.moxy.viewstate.strategy.AddToEndSingleStrategy;
import io.moxy.viewstate.strategy.StateStrategyType;

public interface ViewStateParentStrategyTagView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void method1();

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test2")
    void method2();
}

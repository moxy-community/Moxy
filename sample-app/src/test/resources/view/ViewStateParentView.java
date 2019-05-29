package view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ViewStateParentView extends MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void method1();

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test")
    void method2();
}

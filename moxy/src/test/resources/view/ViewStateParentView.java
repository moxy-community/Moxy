package view;

import io.moxy.MvpView;
import io.moxy.viewstate.strategy.AddToEndSingleStrategy;
import io.moxy.viewstate.strategy.StateStrategyType;

public interface ViewStateParentView extends MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void method1();

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test")
    void method2();
}

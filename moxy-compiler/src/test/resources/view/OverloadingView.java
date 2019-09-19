package view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface OverloadingView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void method(String string);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void method(int number);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void method(Object object);
}
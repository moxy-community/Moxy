package view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface SimpleView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void testEvent();
}
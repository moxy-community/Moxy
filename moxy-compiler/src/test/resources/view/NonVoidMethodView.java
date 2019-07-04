package view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface NonVoidMethodView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    int testEvent();
}
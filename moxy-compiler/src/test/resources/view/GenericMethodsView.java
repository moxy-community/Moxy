package view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface GenericMethodsView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    <T> void generic(T param);

    @StateStrategyType(AddToEndSingleStrategy.class)
    <T extends Number> void genericWithExtends(T param);
}

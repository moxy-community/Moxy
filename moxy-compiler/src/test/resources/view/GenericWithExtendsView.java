package view;

import java.io.Serializable;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface GenericWithExtendsView<T extends Serializable> extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void testEvent(T param);
}

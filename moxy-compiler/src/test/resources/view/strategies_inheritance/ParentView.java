package view.strategies_inheritance;

import moxy.MvpView;
import moxy.viewstate.strategy.StateStrategyType;

import view.strategies_inheritance.strategies.ParentDefaultStrategy;
import view.strategies_inheritance.strategies.Strategy1;

@StateStrategyType(ParentDefaultStrategy.class)
public interface ParentView extends MvpView {
    void parentMethod1(); // ParentDefaultStrategy

    void parentMethod2(); // ParentDefaultStrategy

    void parentMethod3(); // ParentDefaultStrategy

    void parentMethodWithArg(String i); // ParentDefaultStrategy

    void parentMethodWithArg2(String i); // ParentDefaultStrategy

    void parentMethodWithArg3(String i); // ParentDefaultStrategy

    @StateStrategyType(Strategy1.class)
    void parentMethodWithStrategy(); // Strategy1
}

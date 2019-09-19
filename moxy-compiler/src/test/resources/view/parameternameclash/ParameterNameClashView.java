package view.parameternameclash;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ParameterNameClashView extends MvpView {

    void tryView(String view);

    void tryViewCommands(String viewCommands);

    void tryViews(String views);
}
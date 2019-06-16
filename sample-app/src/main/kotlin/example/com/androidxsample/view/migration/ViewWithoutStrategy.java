package example.com.androidxsample.view.migration;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface ViewWithoutStrategy extends MvpView {

    void noStrategyMethod();
}

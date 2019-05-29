package view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StrategiesView extends MvpView {
	@StateStrategyType(SingleStateStrategy.class)
	void singleState();

	@StateStrategyType(OneExecutionStateStrategy.class)
	void oneExecution();

	void withoutStrategy();
}
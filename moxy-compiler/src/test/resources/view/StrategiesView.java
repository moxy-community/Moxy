package view;

import io.moxy.MvpView;
import io.moxy.viewstate.strategy.AddToEndSingleStrategy;
import io.moxy.viewstate.strategy.OneExecutionStateStrategy;
import io.moxy.viewstate.strategy.SingleStateStrategy;
import io.moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StrategiesView extends MvpView {
	@StateStrategyType(SingleStateStrategy.class)
	void singleState();

	@StateStrategyType(OneExecutionStateStrategy.class)
	void oneExecution();

	void withoutStrategy();
}
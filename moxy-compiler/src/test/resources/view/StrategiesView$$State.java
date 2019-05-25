package view;

import io.moxy.viewstate.MvpViewState;
import io.moxy.viewstate.ViewCommand;
import io.moxy.viewstate.strategy.AddToEndSingleStrategy;
import io.moxy.viewstate.strategy.OneExecutionStateStrategy;
import io.moxy.viewstate.strategy.SingleStateStrategy;
import java.lang.Override;

public class StrategiesView$$State extends MvpViewState<StrategiesView> implements StrategiesView {
	@Override
	public void singleState() {
		SingleStateCommand singleStateCommand = new SingleStateCommand();
		mViewCommands.beforeApply(singleStateCommand);

		if (hasNotView()) {
			return;
		}

		for (StrategiesView view : mViews) {
			view.singleState();
		}

		mViewCommands.afterApply(singleStateCommand);
	}

	@Override
	public void oneExecution() {
		OneExecutionCommand oneExecutionCommand = new OneExecutionCommand();
		mViewCommands.beforeApply(oneExecutionCommand);

		if (hasNotView()) {
			return;
		}

		for (StrategiesView view : mViews) {
			view.oneExecution();
		}

		mViewCommands.afterApply(oneExecutionCommand);
	}

	@Override
	public void withoutStrategy() {
		WithoutStrategyCommand withoutStrategyCommand = new WithoutStrategyCommand();
		mViewCommands.beforeApply(withoutStrategyCommand);

		if (hasNotView()) {
			return;
		}

		for (StrategiesView view : mViews) {
			view.withoutStrategy();
		}

		mViewCommands.afterApply(withoutStrategyCommand);
	}

	public class SingleStateCommand extends ViewCommand<StrategiesView> {
		SingleStateCommand() {
			super("singleState", SingleStateStrategy.class);
		}

		@Override
		public void apply(StrategiesView mvpView) {
			mvpView.singleState();
		}
	}

	public class OneExecutionCommand extends ViewCommand<StrategiesView> {
		OneExecutionCommand() {
			super("oneExecution", OneExecutionStateStrategy.class);
		}

		@Override
		public void apply(StrategiesView mvpView) {
			mvpView.oneExecution();
		}
	}

	public class WithoutStrategyCommand extends ViewCommand<StrategiesView> {
		WithoutStrategyCommand() {
			super("withoutStrategy", AddToEndSingleStrategy.class);
		}

		@Override
		public void apply(StrategiesView mvpView) {
			mvpView.withoutStrategy();
		}
	}
}
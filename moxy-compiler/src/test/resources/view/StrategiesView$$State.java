package view;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;

public class StrategiesView$$State extends MvpViewState<StrategiesView> implements StrategiesView {
    @Override
    public void singleState() {
        SingleStateCommand singleStateCommand = new SingleStateCommand();
        viewCommands.beforeApply(singleStateCommand);

        if (hasNotView()) {
            return;
        }

        for (StrategiesView view : views) {
            view.singleState();
        }

        viewCommands.afterApply(singleStateCommand);
    }

    @Override
    public void oneExecution() {
        OneExecutionCommand oneExecutionCommand = new OneExecutionCommand();
        viewCommands.beforeApply(oneExecutionCommand);

        if (hasNotView()) {
            return;
        }

        for (StrategiesView view : views) {
            view.oneExecution();
        }

        viewCommands.afterApply(oneExecutionCommand);
    }

    @Override
    public void withoutStrategy() {
        WithoutStrategyCommand withoutStrategyCommand = new WithoutStrategyCommand();
        viewCommands.beforeApply(withoutStrategyCommand);

        if (hasNotView()) {
            return;
        }

        for (StrategiesView view : views) {
            view.withoutStrategy();
        }

        viewCommands.afterApply(withoutStrategyCommand);
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
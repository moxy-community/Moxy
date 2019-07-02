package view;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import moxy.viewstate.strategy.AddToEndStrategy;

public class SimpleView$$State extends MvpViewState<SimpleView> implements SimpleView {
    @Override
    public void testEvent() {
        TestEventCommand testEventCommand = new TestEventCommand();
        viewCommands.beforeApply(testEventCommand);

        if (hasNotView()) {
            return;
        }

        for (SimpleView view : views) {
            view.testEvent();
        }

        viewCommands.afterApply(testEventCommand);
    }

    public class TestEventCommand extends ViewCommand<SimpleView> {
        TestEventCommand() {
            super("testEvent", AddToEndStrategy.class);
        }

        @Override
        public void apply(SimpleView mvpView) {
            mvpView.testEvent();
        }
    }
}
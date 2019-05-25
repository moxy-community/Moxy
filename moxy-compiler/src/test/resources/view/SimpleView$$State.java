package view;

import io.moxy.viewstate.MvpViewState;
import io.moxy.viewstate.ViewCommand;
import io.moxy.viewstate.strategy.AddToEndStrategy;
import java.lang.Override;

public class SimpleView$$State extends MvpViewState<SimpleView> implements SimpleView {
	@Override
	public void testEvent() {
		TestEventCommand testEventCommand = new TestEventCommand();
		mViewCommands.beforeApply(testEventCommand);

		if (hasNotView()) {
			return;
		}

		for (SimpleView view : mViews) {
			view.testEvent();
		}

		mViewCommands.afterApply(testEventCommand);
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
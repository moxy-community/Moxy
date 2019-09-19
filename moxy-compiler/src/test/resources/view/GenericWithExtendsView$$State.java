package view;

import java.io.Serializable;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import moxy.viewstate.strategy.AddToEndSingleStrategy;

public class GenericWithExtendsView$$State<T extends Serializable> extends MvpViewState<GenericWithExtendsView<T>>
        implements GenericWithExtendsView<T> {

    @Override
    public void testEvent(T param) {
        TestEventCommand testEventCommand = new TestEventCommand(param);
        viewCommands.beforeApply(testEventCommand);

        if (hasNotView()) {
            return;
        }

        for (GenericWithExtendsView<T> view : views) {
            view.testEvent(param);
        }

        viewCommands.afterApply(testEventCommand);
    }

    public class TestEventCommand extends ViewCommand<GenericWithExtendsView<T>> {

        public final T param;

        TestEventCommand(T param) {
            super("testEvent", AddToEndSingleStrategy.class);

            this.param = param;
        }

        @Override
        public void apply(GenericWithExtendsView<T> mvpView) {
            mvpView.testEvent(param);
        }
    }
}
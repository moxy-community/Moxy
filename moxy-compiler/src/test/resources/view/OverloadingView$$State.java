package view;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import moxy.viewstate.strategy.AddToEndSingleStrategy;

public class OverloadingView$$State extends MvpViewState<OverloadingView> implements OverloadingView {

    @Override
    public void method(String string) {
        MethodCommand methodCommand = new MethodCommand(string);
        viewCommands.beforeApply(methodCommand);

        if (hasNotView()) {
            return;
        }

        for (OverloadingView view : views) {
            view.method(string);
        }

        viewCommands.afterApply(methodCommand);
    }

    @Override
    public void method(int number) {
        Method1Command method1Command = new Method1Command(number);
        viewCommands.beforeApply(method1Command);

        if (hasNotView()) {
            return;
        }

        for (OverloadingView view : views) {
            view.method(number);
        }

        viewCommands.afterApply(method1Command);
    }

    @Override
    public void method(Object object) {
        Method2Command method2Command = new Method2Command(object);
        viewCommands.beforeApply(method2Command);

        if (hasNotView()) {
            return;
        }

        for (OverloadingView view : views) {
            view.method(object);
        }

        viewCommands.afterApply(method2Command);
    }

    public class MethodCommand extends ViewCommand<OverloadingView> {

        public final String string;

        MethodCommand(String string) {
            super("method", AddToEndSingleStrategy.class);

            this.string = string;
        }

        @Override
        public void apply(OverloadingView mvpView) {
            mvpView.method(string);
        }
    }

    public class Method1Command extends ViewCommand<OverloadingView> {

        public final int number;

        Method1Command(int number) {
            super("method", AddToEndSingleStrategy.class);

            this.number = number;
        }

        @Override
        public void apply(OverloadingView mvpView) {
            mvpView.method(number);
        }
    }

    public class Method2Command extends ViewCommand<OverloadingView> {

        public final Object object;

        Method2Command(Object object) {
            super("method", AddToEndSingleStrategy.class);

            this.object = object;
        }

        @Override
        public void apply(OverloadingView mvpView) {
            mvpView.method(object);
        }
    }
}
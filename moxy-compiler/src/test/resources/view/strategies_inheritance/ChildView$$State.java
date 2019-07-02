package view.strategies_inheritance;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import view.strategies_inheritance.strategies.ChildDefaultStrategy;
import view.strategies_inheritance.strategies.Strategy1;
import view.strategies_inheritance.strategies.Strategy2;

public class ChildView$$State extends MvpViewState<ChildView> implements ChildView {
    @Override
    public void parentMethod1() {
        ParentMethod1Command parentMethod1Command = new ParentMethod1Command();
        viewCommands.beforeApply(parentMethod1Command);

        if (hasNotView()) {
            return;
        }

        for (ChildView view : views) {
            view.parentMethod1();
        }

        viewCommands.afterApply(parentMethod1Command);
    }

    @Override
    public void parentMethod2() {
        ParentMethod2Command parentMethod2Command = new ParentMethod2Command();
        viewCommands.beforeApply(parentMethod2Command);

        if (hasNotView()) {
            return;
        }

        for (ChildView view : views) {
            view.parentMethod2();
        }

        viewCommands.afterApply(parentMethod2Command);
    }

    @Override
    public void childMethod() {
        ChildMethodCommand childMethodCommand = new ChildMethodCommand();
        viewCommands.beforeApply(childMethodCommand);

        if (hasNotView()) {
            return;
        }

        for (ChildView view : views) {
            view.childMethod();
        }

        viewCommands.afterApply(childMethodCommand);
    }

    @Override
    public void childMethodWithStrategy() {
        ChildMethodWithStrategyCommand childMethodWithStrategyCommand =
            new ChildMethodWithStrategyCommand();
        viewCommands.beforeApply(childMethodWithStrategyCommand);

        if (hasNotView()) {
            return;
        }

        for (ChildView view : views) {
            view.childMethodWithStrategy();
        }

        viewCommands.afterApply(childMethodWithStrategyCommand);
    }

    @Override
    public void parentMethod3() {
        ParentMethod3Command parentMethod3Command = new ParentMethod3Command();
        viewCommands.beforeApply(parentMethod3Command);

        if (hasNotView()) {
            return;
        }

        for (ChildView view : views) {
            view.parentMethod3();
        }

        viewCommands.afterApply(parentMethod3Command);
    }

    @Override
    public void parentMethodWithStrategy() {
        ParentMethodWithStrategyCommand parentMethodWithStrategyCommand =
            new ParentMethodWithStrategyCommand();
        viewCommands.beforeApply(parentMethodWithStrategyCommand);

        if (hasNotView()) {
            return;
        }

        for (ChildView view : views) {
            view.parentMethodWithStrategy();
        }

        viewCommands.afterApply(parentMethodWithStrategyCommand);
    }

    public class ParentMethod1Command extends ViewCommand<ChildView> {
        ParentMethod1Command() {
            super("parentMethod1", ChildDefaultStrategy.class);
        }

        @Override
        public void apply(ChildView mvpView) {
            mvpView.parentMethod1();
        }
    }

    public class ParentMethod2Command extends ViewCommand<ChildView> {
        ParentMethod2Command() {
            super("parentMethod2", Strategy2.class);
        }

        @Override
        public void apply(ChildView mvpView) {
            mvpView.parentMethod2();
        }
    }

    public class ChildMethodCommand extends ViewCommand<ChildView> {
        ChildMethodCommand() {
            super("childMethod", ChildDefaultStrategy.class);
        }

        @Override
        public void apply(ChildView mvpView) {
            mvpView.childMethod();
        }
    }

    public class ChildMethodWithStrategyCommand extends ViewCommand<ChildView> {
        ChildMethodWithStrategyCommand() {
            super("childMethodWithStrategy", Strategy2.class);
        }

        @Override
        public void apply(ChildView mvpView) {
            mvpView.childMethodWithStrategy();
        }
    }

    public class ParentMethod3Command extends ViewCommand<ChildView> {
        ParentMethod3Command() {
            super("parentMethod3", ChildDefaultStrategy.class);
        }

        @Override
        public void apply(ChildView mvpView) {
            mvpView.parentMethod3();
        }
    }

    public class ParentMethodWithStrategyCommand extends ViewCommand<ChildView> {
        ParentMethodWithStrategyCommand() {
            super("parentMethodWithStrategy", Strategy1.class);
        }

        @Override
        public void apply(ChildView mvpView) {
            mvpView.parentMethodWithStrategy();
        }
    }
}
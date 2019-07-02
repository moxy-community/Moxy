package view.strategies_inheritance;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import view.strategies_inheritance.strategies.ParentDefaultStrategy;
import view.strategies_inheritance.strategies.Strategy1;

public class ParentView$$State extends MvpViewState<ParentView> implements ParentView {

    @Override
    public void parentMethod1() {
        ParentMethod1Command parentMethod1Command = new ParentMethod1Command();
        viewCommands.beforeApply(parentMethod1Command);

        if (hasNotView()) {
            return;
        }

        for (ParentView view : views) {
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

        for (ParentView view : views) {
            view.parentMethod2();
        }

        viewCommands.afterApply(parentMethod2Command);
    }

    @Override
    public void parentMethod3() {
        ParentMethod3Command parentMethod3Command = new ParentMethod3Command();
        viewCommands.beforeApply(parentMethod3Command);

        if (hasNotView()) {
            return;
        }

        for (ParentView view : views) {
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

        for (ParentView view : views) {
            view.parentMethodWithStrategy();
        }

        viewCommands.afterApply(parentMethodWithStrategyCommand);
    }

    public class ParentMethod1Command extends ViewCommand<ParentView> {

        ParentMethod1Command() {
            super("parentMethod1", ParentDefaultStrategy.class);
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethod1();
        }
    }

    public class ParentMethod2Command extends ViewCommand<ParentView> {

        ParentMethod2Command() {
            super("parentMethod2", ParentDefaultStrategy.class);
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethod2();
        }
    }

    public class ParentMethod3Command extends ViewCommand<ParentView> {

        ParentMethod3Command() {
            super("parentMethod3", ParentDefaultStrategy.class);
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethod3();
        }
    }

    public class ParentMethodWithStrategyCommand extends ViewCommand<ParentView> {

        ParentMethodWithStrategyCommand() {
            super("parentMethodWithStrategy", Strategy1.class);
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethodWithStrategy();
        }
    }
}
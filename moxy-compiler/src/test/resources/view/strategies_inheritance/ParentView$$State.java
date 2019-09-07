package view.strategies_inheritance;

import java.lang.Override;
import java.lang.String;
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
    public void parentMethodWithArg(String i) {
        ParentMethodWithArgCommand parentMethodWithArgCommand = new ParentMethodWithArgCommand(i);
        viewCommands.beforeApply(parentMethodWithArgCommand);

        if (hasNotView()) {
            return;
        }

        for (ParentView view : views) {
            view.parentMethodWithArg(i);
        }

        viewCommands.afterApply(parentMethodWithArgCommand);
    }

    @Override
    public void parentMethodWithArg2(String i) {
        ParentMethodWithArg2Command parentMethodWithArg2Command = new ParentMethodWithArg2Command(i);
        viewCommands.beforeApply(parentMethodWithArg2Command);

        if (hasNotView()) {
            return;
        }

        for (ParentView view : views) {
            view.parentMethodWithArg2(i);
        }

        viewCommands.afterApply(parentMethodWithArg2Command);
    }

    @Override
    public void parentMethodWithArg3(String i) {
        ParentMethodWithArg3Command parentMethodWithArg3Command = new ParentMethodWithArg3Command(i);
        viewCommands.beforeApply(parentMethodWithArg3Command);

        if (hasNotView()) {
            return;
        }

        for (ParentView view : views) {
            view.parentMethodWithArg3(i);
        }

        viewCommands.afterApply(parentMethodWithArg3Command);
    }

    @Override
    public void parentMethodWithStrategy() {
        ParentMethodWithStrategyCommand parentMethodWithStrategyCommand = new ParentMethodWithStrategyCommand();
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

    public class ParentMethodWithArgCommand extends ViewCommand<ParentView> {
        public final String i;

        ParentMethodWithArgCommand(String i) {
            super("parentMethodWithArg", ParentDefaultStrategy.class);

            this.i = i;
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethodWithArg(i);
        }
    }

    public class ParentMethodWithArg2Command extends ViewCommand<ParentView> {
        public final String i;

        ParentMethodWithArg2Command(String i) {
            super("parentMethodWithArg2", ParentDefaultStrategy.class);

            this.i = i;
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethodWithArg2(i);
        }
    }

    public class ParentMethodWithArg3Command extends ViewCommand<ParentView> {
        public final String i;

        ParentMethodWithArg3Command(String i) {
            super("parentMethodWithArg3", ParentDefaultStrategy.class);

            this.i = i;
        }

        @Override
        public void apply(ParentView mvpView) {
            mvpView.parentMethodWithArg3(i);
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
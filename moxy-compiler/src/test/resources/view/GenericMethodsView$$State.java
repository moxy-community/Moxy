package view;

import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import moxy.viewstate.strategy.AddToEndStrategy;

public class GenericMethodsView$$State extends MvpViewState<GenericMethodsView> implements GenericMethodsView {
    @Override
    public <T> void generic(T param) {
        GenericCommand genericCommand = new GenericCommand(param);
        mViewCommands.beforeApply(genericCommand);

        if (hasNotView()) {
            return;
        }

        for (GenericMethodsView view : mViews) {
            view.generic(param);
        }

        mViewCommands.afterApply(genericCommand);
    }

    @Override
    public <T extends Number> void genericWithExtends(T param) {
        GenericWithExtendsCommand genericWithExtendsCommand = new GenericWithExtendsCommand(param);
        mViewCommands.beforeApply(genericWithExtendsCommand);

        if (hasNotView()) {
            return;
        }

        for (GenericMethodsView view : mViews) {
            view.genericWithExtends(param);
        }

        mViewCommands.afterApply(genericWithExtendsCommand);
    }

    public class GenericCommand<T> extends ViewCommand<GenericMethodsView> {
        public final T param;

        GenericCommand(T param) {
            super("generic", AddToEndStrategy.class);

            this.param = param;
        }

        @Override
        public void apply(GenericMethodsView mvpView) {
            mvpView.generic(param);
        }
    }

    public class GenericWithExtendsCommand<T extends Number>
        extends ViewCommand<GenericMethodsView> {
        public final T param;

        GenericWithExtendsCommand(T param) {
            super("genericWithExtends", AddToEndStrategy.class);

            this.param = param;
        }

        @Override
        public void apply(GenericMethodsView mvpView) {
            mvpView.genericWithExtends(param);
        }
    }
}
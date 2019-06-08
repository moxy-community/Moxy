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
    mViewCommands.beforeApply(parentMethod1Command);

    if (hasNotView()) {
      return;
    }

    for (ChildView view : mViews) {
      view.parentMethod1();
    }

    mViewCommands.afterApply(parentMethod1Command);
  }

  @Override
  public void parentMethod2() {
    ParentMethod2Command parentMethod2Command = new ParentMethod2Command();
    mViewCommands.beforeApply(parentMethod2Command);

    if (hasNotView()) {
      return;
    }

    for (ChildView view : mViews) {
      view.parentMethod2();
    }

    mViewCommands.afterApply(parentMethod2Command);
  }

  @Override
  public void childMethod() {
    ChildMethodCommand childMethodCommand = new ChildMethodCommand();
    mViewCommands.beforeApply(childMethodCommand);

    if (hasNotView()) {
      return;
    }

    for (ChildView view : mViews) {
      view.childMethod();
    }

    mViewCommands.afterApply(childMethodCommand);
  }

  @Override
  public void childMethodWithStrategy() {
    ChildMethodWithStrategyCommand childMethodWithStrategyCommand =
        new ChildMethodWithStrategyCommand();
    mViewCommands.beforeApply(childMethodWithStrategyCommand);

    if (hasNotView()) {
      return;
    }

    for (ChildView view : mViews) {
      view.childMethodWithStrategy();
    }

    mViewCommands.afterApply(childMethodWithStrategyCommand);
  }

  @Override
  public void parentMethod3() {
    ParentMethod3Command parentMethod3Command = new ParentMethod3Command();
    mViewCommands.beforeApply(parentMethod3Command);

    if (hasNotView()) {
      return;
    }

    for (ChildView view : mViews) {
      view.parentMethod3();
    }

    mViewCommands.afterApply(parentMethod3Command);
  }

  @Override
  public void parentMethodWithStrategy() {
    ParentMethodWithStrategyCommand parentMethodWithStrategyCommand =
        new ParentMethodWithStrategyCommand();
    mViewCommands.beforeApply(parentMethodWithStrategyCommand);

    if (hasNotView()) {
      return;
    }

    for (ChildView view : mViews) {
      view.parentMethodWithStrategy();
    }

    mViewCommands.afterApply(parentMethodWithStrategyCommand);
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
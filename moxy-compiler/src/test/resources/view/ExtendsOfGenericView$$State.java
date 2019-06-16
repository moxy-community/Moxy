package view;

import java.io.Serializable;
import moxy.viewstate.MvpViewState;
import moxy.viewstate.ViewCommand;
import moxy.viewstate.strategy.AddToEndStrategy;

public class ExtendsOfGenericView$$State extends MvpViewState<ExtendsOfGenericView>
  implements ExtendsOfGenericView {
  @Override
  public void testEvent(Serializable param) {
    TestEventCommand testEventCommand = new TestEventCommand(param);
    mViewCommands.beforeApply(testEventCommand);

    if (hasNotView()) {
      return;
    }

    for (ExtendsOfGenericView view : mViews) {
      view.testEvent(param);
    }

    mViewCommands.afterApply(testEventCommand);
  }

  public class TestEventCommand extends ViewCommand<ExtendsOfGenericView> {
    public final Serializable param;

    TestEventCommand(Serializable param) {
      super("testEvent", AddToEndStrategy.class);

      this.param = param;
    }

    @Override
    public void apply(ExtendsOfGenericView mvpView) {
      mvpView.testEvent(param);
    }
  }
}
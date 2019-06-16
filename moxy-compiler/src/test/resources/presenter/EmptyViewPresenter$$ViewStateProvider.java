package presenter;

import moxy.MvpView;
import moxy.ViewStateProvider;
import moxy.viewstate.MvpViewState;
import view.EmptyView$$State;

public class EmptyViewPresenter$$ViewStateProvider extends ViewStateProvider {

  @Override
  public MvpViewState<? extends MvpView> getViewState() {
    return new EmptyView$$State();
  }
}

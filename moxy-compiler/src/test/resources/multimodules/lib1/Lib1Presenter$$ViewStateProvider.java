package multimodules.lib1;

import moxy.MvpView;
import moxy.ViewStateProvider;
import moxy.viewstate.MvpViewState;

public class Lib1Presenter$$ViewStateProvider extends ViewStateProvider {
  @Override
  public MvpViewState<? extends MvpView> getViewState() {
    return new Lib1View$$State();
  }
}

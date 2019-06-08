package target;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import presenter.EmptyViewPresenter;
import view.EmptyView;

public class SimpleProvidePresenterTarget implements EmptyView {
  @InjectPresenter
  EmptyViewPresenter presenter;

  @ProvidePresenter
  EmptyViewPresenter providePresenter() {
    return new EmptyViewPresenter();
  }
}

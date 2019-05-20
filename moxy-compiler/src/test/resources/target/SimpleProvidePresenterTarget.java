package target;

import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.ProvidePresenter;

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

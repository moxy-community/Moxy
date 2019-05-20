package target;

import io.moxy.presenter.InjectPresenter;

import presenter.EmptyViewPresenter;
import view.EmptyView;

public class SimpleInjectPresenterTarget implements EmptyView {
	@InjectPresenter
	EmptyViewPresenter presenter;
}

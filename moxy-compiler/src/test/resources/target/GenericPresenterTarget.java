package target;

import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.ProvidePresenter;

import presenter.GenericPresenter;
import view.GenericView;

public class GenericPresenterTarget implements GenericView<String> {

	@InjectPresenter
	GenericPresenter<String> presenter;

	@ProvidePresenter
	GenericPresenter<String> providePresenter() {
		return new GenericPresenter<>();
	}

	@Override
	public void testEvent(String param) {
	}
}
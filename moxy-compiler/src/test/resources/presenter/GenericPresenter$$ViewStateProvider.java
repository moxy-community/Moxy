package presenter;

import io.moxy.MvpView;
import io.moxy.ViewStateProvider;
import io.moxy.viewstate.MvpViewState;

import view.GenericView$$State;

public class GenericPresenter$$ViewStateProvider extends ViewStateProvider {

	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new GenericView$$State();
	}
}

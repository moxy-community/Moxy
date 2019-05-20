package presenter;

import io.moxy.MvpView;
import io.moxy.ViewStateProvider;
import io.moxy.viewstate.MvpViewState;

import view.EmptyView$$State;

public class EmptyViewPresenter$$ViewStateProvider extends ViewStateProvider {

	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new EmptyView$$State();
	}
}

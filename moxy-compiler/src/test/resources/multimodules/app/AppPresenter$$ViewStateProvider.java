package multimodules.app;

import io.moxy.MvpView;
import io.moxy.ViewStateProvider;
import io.moxy.viewstate.MvpViewState;

public class AppPresenter$$ViewStateProvider extends ViewStateProvider {
	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new AppView$$State();
	}
}

package multimodules.lib1;

import io.moxy.MvpView;
import io.moxy.ViewStateProvider;
import io.moxy.viewstate.MvpViewState;

public class Lib1Presenter$$ViewStateProvider extends ViewStateProvider {
	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new Lib1View$$State();
	}
}

package presenter;

import moxy.MvpView;
import moxy.ViewStateProvider;
import moxy.viewstate.MvpViewState;
import view.GenericView$$State;

public class GenericPresenter$$ViewStateProvider extends ViewStateProvider {

    @Override
    public MvpViewState<? extends MvpView> getViewState() {
        return new GenericView$$State();
    }
}

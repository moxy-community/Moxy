package presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import view.EmptyView;

class PresenterWrapper {
    @InjectViewState
    public static class EmptyViewPresenter extends MvpPresenter<EmptyView> {
    }
}
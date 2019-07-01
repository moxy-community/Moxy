package presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import view.GenericView;

@InjectViewState
public class GenericPresenter<T> extends MvpPresenter<GenericView<T>> {
}
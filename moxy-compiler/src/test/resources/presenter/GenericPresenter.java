package presenter;

import io.moxy.InjectViewState;
import io.moxy.MvpPresenter;

import view.GenericView;

@InjectViewState
public class GenericPresenter<T> extends MvpPresenter<GenericView<T>> {
}
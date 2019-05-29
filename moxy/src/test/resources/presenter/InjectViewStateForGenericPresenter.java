package presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.view.CounterTestView;

@InjectViewState
public class InjectViewStateForGenericPresenter<T extends CounterTestView> extends MvpPresenter<T> {

}

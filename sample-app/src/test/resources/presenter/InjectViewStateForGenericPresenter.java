package presenter;

import io.moxy.InjectViewState;
import io.moxy.MvpPresenter;
import io.moxy.view.CounterTestView;

@InjectViewState
public class InjectViewStateForGenericPresenter<T extends CounterTestView> extends MvpPresenter<T> {

}

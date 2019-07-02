package presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.view.CounterTestView;

@InjectViewState
public class InjectViewStateForClassPresenter extends MvpPresenter<CounterTestView> {

}

package presenter;

import io.moxy.MvpPresenter;
import io.moxy.view.CounterTestView;
import io.moxy.view.TestView;

public class WithViewGenericPresenter2<T extends TestView, S extends CounterTestView> extends MvpPresenter<T> {

}

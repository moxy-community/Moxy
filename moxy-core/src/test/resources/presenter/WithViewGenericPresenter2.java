package presenter;

import moxy.MvpPresenter;
import moxy.view.CounterTestView;
import moxy.view.TestView;

public class WithViewGenericPresenter2<T extends TestView, S extends CounterTestView> extends MvpPresenter<T> {

}

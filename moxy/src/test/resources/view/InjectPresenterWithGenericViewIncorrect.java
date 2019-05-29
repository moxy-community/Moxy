package view;

import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.view.CounterTestView;
import moxy.view.TestViewChild2;
import presenter.WithViewGenericPresenter;

public class InjectPresenterWithGenericViewIncorrect extends TestViewChild2<MvpView> {

    @InjectPresenter
    WithViewGenericPresenter<CounterTestView, InjectPresenterWithGenericViewIncorrect> mPresenter;

    public void testEvent() {

    }
}

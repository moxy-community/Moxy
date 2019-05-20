package view;

import io.moxy.MvpView;
import io.moxy.presenter.InjectPresenter;
import io.moxy.view.CounterTestView;
import io.moxy.view.TestViewChild2;
import presenter.WithViewGenericPresenter;

public class InjectPresenterWithGenericViewIncorrect extends TestViewChild2<MvpView> {

    @InjectPresenter
    WithViewGenericPresenter<CounterTestView, InjectPresenterWithGenericViewIncorrect> mPresenter;

    public void testEvent() {

    }
}

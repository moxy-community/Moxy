package view;

import io.moxy.presenter.InjectPresenter;
import io.moxy.view.CounterTestView;
import presenter.WithViewGenericPresenter;

public class InjectPresenterWithGenericView extends CounterTestView {

    @InjectPresenter
    WithViewGenericPresenter<InjectPresenterWithGenericView, CounterTestView> mPresenter;

    public void testEvent() {

    }
}

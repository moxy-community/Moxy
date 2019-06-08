package view;

import moxy.presenter.InjectPresenter;
import moxy.view.CounterTestView;
import presenter.WithViewGenericPresenter;

public class InjectPresenterWithGenericView extends CounterTestView {

  @InjectPresenter
  WithViewGenericPresenter<InjectPresenterWithGenericView, CounterTestView> mPresenter;

  public void testEvent() {

  }
}

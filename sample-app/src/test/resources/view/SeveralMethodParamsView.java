package view;

import moxy.MvpView;
import moxy.factory.MockPresenterFactory;
import moxy.presenter.InjectPresenter;
import moxy.presenter.InjectViewStatePresenter;
import moxy.presenter.PresenterType;

public class SeveralMethodParamsView implements MvpView, params.SeveralMethodParams {

  @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
  public InjectViewStatePresenter mInjectViewStatePresenter;

  @Override
  public void method1() {

  }

  @Override
  public void method2() {

  }
}
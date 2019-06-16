package view;

import moxy.MvpView;
import moxy.factory.MockPresenterFactory;
import moxy.presenter.InjectPresenter;
import moxy.presenter.InjectViewStatePresenter;
import moxy.presenter.PresenterType;
import params.IncorrectParametersParams;

public class IncorrectParametersParamsView implements MvpView, IncorrectParametersParams {

  @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
  public InjectViewStatePresenter mInjectViewStatePresenter;

  @Override
  public void method1(final Integer i) {

  }
}

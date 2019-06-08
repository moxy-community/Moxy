package view;

import moxy.MvpView;
import moxy.factory.MockPresenterFactory;
import moxy.presenter.InjectPresenter;
import moxy.presenter.PresenterType;
import params.Params1;
import presenter.PositiveParamsViewPresenter;

public class PositiveParamsView implements MvpView, Params1 {

  @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
  public PositiveParamsViewPresenter<PositiveParamsView> mInjectViewStatePresenter;

  @Override
  public String mockParams1(final String presenterId) {
    return null;
  }
}
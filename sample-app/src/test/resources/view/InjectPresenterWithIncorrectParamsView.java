package view;

import moxy.factory.MockPresenterFactory2;
import moxy.presenter.InjectPresenter;
import moxy.presenter.PresenterType;
import moxy.presenter.TestViewPresenter;
import moxy.view.TestView;
import params.Params1;

public class InjectPresenterWithIncorrectParamsView implements TestView, Params1 {

  @InjectPresenter(factory = MockPresenterFactory2.class, presenterId = "Test", type = PresenterType.LOCAL)
  public TestViewPresenter mPresenter;

  @Override
  public void testEvent() {

  }

  @Override
  public String mockParams1(final String presenterId) {
    return null;
  }
}

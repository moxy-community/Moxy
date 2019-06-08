package view;

import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.TestViewPresenter;

public class InjectPresenterWithIncorrectViewView implements MvpView {

  @InjectPresenter
  public TestViewPresenter mTestViewPresenter;
}

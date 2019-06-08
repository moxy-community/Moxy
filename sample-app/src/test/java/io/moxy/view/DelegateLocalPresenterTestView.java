package moxy.view;

import moxy.presenter.InjectPresenter;
import moxy.presenter.InjectViewStatePresenter;

public class DelegateLocalPresenterTestView extends CounterTestView {
  @InjectPresenter
  public InjectViewStatePresenter mInjectViewStatePresenter;
}
package moxy.inheritance_test.resources;

import moxy.presenter.InjectPresenter;

public class SuperViewWithInject extends ViewWithoutInject implements TestView {

  @InjectPresenter
  public TestPresenter presenter;
}

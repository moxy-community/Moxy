package moxy.memmory_leak_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;

public class TestViewImplementation implements TestView {

  @InjectPresenter
  public TestPresenter presenter;

  public MvpDelegate<TestViewImplementation> delegate;
}

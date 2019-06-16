package moxy.provide_methods_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class SuperView implements TestView {

  @InjectPresenter
  public TestPresenter oneLocalPresenter;

  public TestPresenter oneLocalProvidedPresenter;

  @InjectPresenter
  public TestPresenter secondLocalPresenter;

  @InjectPresenter(presenterId = "one_global")
  public TestPresenter oneGlobalPresenter;

  @InjectPresenter(presenterId = "second_global")
  public TestPresenter secondGlobalPresenter;

  public MvpDelegate<SuperView> delegate;

  @ProvidePresenter
  public TestPresenter provideLocalPresenter() {
    oneLocalProvidedPresenter = new TestPresenter();
    return oneLocalProvidedPresenter;
  }
}

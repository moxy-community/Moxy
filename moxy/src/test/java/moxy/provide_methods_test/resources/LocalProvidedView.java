package moxy.provide_methods_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class LocalProvidedView implements TestView {

  @InjectPresenter
  public TestPresenter oneLocalPresenter;

  public TestPresenter oneLocalProvidedPresenter;

  public MvpDelegate<LocalProvidedView> delegate;

  @ProvidePresenter
  TestPresenter provideLocalPresenter() {
    oneLocalProvidedPresenter = new TestPresenter();
    return oneLocalProvidedPresenter;
  }
}

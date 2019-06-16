package moxy.provide_methods_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class TwoLocalProvidedView implements TestView {

  @InjectPresenter
  public TestPresenter oneLocalPresenter;

  @InjectPresenter
  public TestPresenter secondLocalPresenter;

  public MvpDelegate<TwoLocalProvidedView> delegate;

  @ProvidePresenter
  TestPresenter provideLocalPresenter() {
    return new TestPresenter();
  }
}

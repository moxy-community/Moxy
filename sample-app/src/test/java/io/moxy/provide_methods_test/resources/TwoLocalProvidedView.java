package moxy.provide_methods_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class TwoLocalProvidedView implements TestView {

  @InjectPresenter
  public moxy.provide_methods_test.resources.TestPresenter oneLocalPresenter;

  @InjectPresenter
  public moxy.provide_methods_test.resources.TestPresenter secondLocalPresenter;

  public MvpDelegate<moxy.provide_methods_test.resources.TwoLocalProvidedView> delegate;

  @ProvidePresenter
  moxy.provide_methods_test.resources.TestPresenter provideLocalPresenter() {
    return new TestPresenter();
  }
}

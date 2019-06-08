package moxy.provide_methods_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class LocalProvidedView implements TestView {

  @InjectPresenter
  public moxy.provide_methods_test.resources.TestPresenter oneLocalPresenter;

  public moxy.provide_methods_test.resources.TestPresenter oneLocalProvidedPresenter;

  public MvpDelegate<moxy.provide_methods_test.resources.LocalProvidedView> delegate;

  @ProvidePresenter
  moxy.provide_methods_test.resources.TestPresenter provideLocalPresenter() {
    oneLocalProvidedPresenter = new TestPresenter();
    return oneLocalProvidedPresenter;
  }
}

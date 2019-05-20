package io.moxy.provide_methods_test.resources;

import io.moxy.MvpDelegate;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.ProvidePresenter;

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

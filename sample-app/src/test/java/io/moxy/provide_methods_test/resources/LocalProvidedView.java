package io.moxy.provide_methods_test.resources;

import io.moxy.MvpDelegate;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.ProvidePresenter;

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

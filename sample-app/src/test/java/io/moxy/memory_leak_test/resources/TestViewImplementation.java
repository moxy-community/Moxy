package io.moxy.memory_leak_test.resources;

import io.moxy.MvpDelegate;
import io.moxy.presenter.InjectPresenter;

public class TestViewImplementation implements TestView {

    @InjectPresenter
    public TestPresenter presenter;

    public MvpDelegate<TestViewImplementation> delegate;
}

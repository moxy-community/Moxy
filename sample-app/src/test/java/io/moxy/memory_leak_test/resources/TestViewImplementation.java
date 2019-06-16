package moxy.memory_leak_test.resources;

import moxy.MvpDelegate;
import moxy.memmory_leak_test.resources.TestPresenter;
import moxy.memmory_leak_test.resources.TestView;
import moxy.presenter.InjectPresenter;

public class TestViewImplementation implements TestView {

    @InjectPresenter
    public TestPresenter presenter;

    public MvpDelegate<moxy.memmory_leak_test.resources.TestViewImplementation> delegate;
}

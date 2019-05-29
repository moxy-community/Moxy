package moxy.provide_methods_test.resources;

import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import moxy.provide_methods_test.resources.TestPresenter;
import moxy.provide_methods_test.resources.TestView;

public class SuperView implements TestView {

    @InjectPresenter
    public moxy.provide_methods_test.resources.TestPresenter oneLocalPresenter;

    public moxy.provide_methods_test.resources.TestPresenter oneLocalProvidedPresenter;

    @InjectPresenter
    public moxy.provide_methods_test.resources.TestPresenter secondLocalPresenter;

    @InjectPresenter(presenterId = "one_global")
    public moxy.provide_methods_test.resources.TestPresenter oneGlobalPresenter;

    @InjectPresenter(presenterId = "second_global")
    public moxy.provide_methods_test.resources.TestPresenter secondGlobalPresenter;

    public MvpDelegate<moxy.provide_methods_test.resources.SuperView> delegate;

    @ProvidePresenter
    public moxy.provide_methods_test.resources.TestPresenter provideLocalPresenter() {
        oneLocalProvidedPresenter = new TestPresenter();
        return oneLocalProvidedPresenter;
    }
}

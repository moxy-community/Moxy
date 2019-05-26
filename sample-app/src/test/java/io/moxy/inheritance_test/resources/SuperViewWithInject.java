package io.moxy.inheritance_test.resources;

import io.moxy.presenter.InjectPresenter;

public class SuperViewWithInject extends ViewWithoutInject implements TestView {

    @InjectPresenter
    public TestPresenter presenter;
}

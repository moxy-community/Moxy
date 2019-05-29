package moxy.inheritance_test.resources;

import moxy.inheritance_test.resources.TestPresenter;
import moxy.inheritance_test.resources.TestView;
import moxy.inheritance_test.resources.ViewWithoutInject;
import moxy.presenter.InjectPresenter;

public class SuperViewWithInject extends ViewWithoutInject implements TestView {

    @InjectPresenter
    public TestPresenter presenter;
}

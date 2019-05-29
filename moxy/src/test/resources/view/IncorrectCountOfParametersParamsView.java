package view;

import moxy.MvpView;
import moxy.factory.MockPresenterFactory;
import moxy.presenter.InjectPresenter;
import moxy.presenter.InjectViewStatePresenter;
import moxy.presenter.PresenterType;
import params.IncorrectCountOfParametersParams;

public class IncorrectCountOfParametersParamsView implements MvpView, IncorrectCountOfParametersParams {

    @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
    public InjectViewStatePresenter mInjectViewStatePresenter;

    @Override
    public void method1(final String s1, String s2) {

    }
}

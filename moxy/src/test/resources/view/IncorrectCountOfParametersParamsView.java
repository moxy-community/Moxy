package view;

import io.moxy.MvpView;
import io.moxy.factory.MockPresenterFactory;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.InjectViewStatePresenter;
import io.moxy.presenter.PresenterType;
import params.IncorrectCountOfParametersParams;

public class IncorrectCountOfParametersParamsView implements MvpView, IncorrectCountOfParametersParams {

    @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
    public InjectViewStatePresenter mInjectViewStatePresenter;

    @Override
    public void method1(final String s1, String s2) {

    }
}

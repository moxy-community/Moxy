package view;

import io.moxy.MvpView;
import io.moxy.factory.MockPresenterFactory;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.InjectViewStatePresenter;
import io.moxy.presenter.PresenterType;
import params.IncorrectParametersParams;

public class IncorrectParametersParamsView implements MvpView, IncorrectParametersParams {

    @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
    public InjectViewStatePresenter mInjectViewStatePresenter;

    @Override
    public void method1(final Integer i) {

    }
}

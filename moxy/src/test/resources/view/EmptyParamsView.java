package moxy.compiler.view;

import moxy.MvpPresenter;
import moxy.MvpView;
import moxy.factory.MockPresenterFactory;
import moxy.presenter.InjectPresenter;
import moxy.presenter.PresenterType;
import params.EmptyParams;

public class EmptyParamsView implements MvpView, EmptyParams {

    @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
    public MvpPresenter<MvpView> mInjectViewStatePresenter;
}
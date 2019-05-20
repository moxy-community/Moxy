package io.moxy.compiler.view;

import io.moxy.MvpPresenter;
import io.moxy.MvpView;
import io.moxy.factory.MockPresenterFactory;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.PresenterType;
import params.EmptyParams;

public class EmptyParamsView implements MvpView, EmptyParams {

    @InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
    public MvpPresenter<MvpView> mInjectViewStatePresenter;
}
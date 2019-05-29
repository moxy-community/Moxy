package view;

import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import presenter.PresenterWithoutEmptyConstructor;

public class InjectPresenterWithoutEmptyConstructorView implements MvpView {

    @InjectPresenter
    public PresenterWithoutEmptyConstructor<InjectPresenterWithoutEmptyConstructorView>
            mPresenterWithoutEmptyConstructorViewPresenterWithoutEmptyConstructor;
}

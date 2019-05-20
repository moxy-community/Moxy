package view;

import io.moxy.MvpView;
import io.moxy.presenter.InjectPresenter;
import presenter.PresenterWithoutEmptyConstructor;

public class InjectPresenterWithoutEmptyConstructorView implements MvpView {

    @InjectPresenter
    public PresenterWithoutEmptyConstructor<InjectPresenterWithoutEmptyConstructorView>
            mPresenterWithoutEmptyConstructorViewPresenterWithoutEmptyConstructor;
}

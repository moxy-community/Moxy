package view;

import io.moxy.MvpView;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.TestViewPresenter;

public class InjectPresenterWithIncorrectViewView implements MvpView {

    @InjectPresenter
    public TestViewPresenter mTestViewPresenter;
}

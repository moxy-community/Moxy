package view;

import io.moxy.MvpPresenter;
import io.moxy.MvpView;
import io.moxy.presenter.InjectPresenter;

public class InjectPresenterAnnotationInsideNotMvpViewClass {

    @InjectPresenter
    public MvpPresenter<MvpView> mObject;
}

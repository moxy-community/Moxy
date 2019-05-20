package view;

import io.moxy.MvpView;
import io.moxy.presenter.InjectPresenter;

public class ObjectInjectPresenterView implements MvpView {

    @InjectPresenter
    public Object mObject;
}

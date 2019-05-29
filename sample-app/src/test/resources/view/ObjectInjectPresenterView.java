package view;

import moxy.MvpView;
import moxy.presenter.InjectPresenter;

public class ObjectInjectPresenterView implements MvpView {

    @InjectPresenter
    public Object mObject;
}

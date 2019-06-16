package view;

import moxy.MvpPresenter;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;

public class InjectPresenterAnnotationInsideNotMvpViewClass {

  @InjectPresenter
  public MvpPresenter<MvpView> mObject;
}

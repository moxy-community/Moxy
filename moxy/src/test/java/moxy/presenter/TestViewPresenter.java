package moxy.presenter;

import moxy.MvpPresenter;
import moxy.view.TestView;

public class TestViewPresenter extends MvpPresenter<TestView> {

  public void testEvent() {
    getViewState().testEvent();
  }
}

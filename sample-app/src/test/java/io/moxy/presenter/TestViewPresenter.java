package io.moxy.presenter;

import io.moxy.MvpPresenter;
import io.moxy.view.TestView;

public class TestViewPresenter extends MvpPresenter<TestView> {

    public void testEvent() {
        getViewState().testEvent();
    }
}

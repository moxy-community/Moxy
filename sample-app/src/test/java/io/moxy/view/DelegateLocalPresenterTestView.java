package io.moxy.view;

import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.InjectViewStatePresenter;

public class DelegateLocalPresenterTestView extends CounterTestView {
    @InjectPresenter
    public InjectViewStatePresenter mInjectViewStatePresenter;
}
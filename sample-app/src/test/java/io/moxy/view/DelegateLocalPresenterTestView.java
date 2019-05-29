package moxy.view;

import moxy.presenter.InjectPresenter;
import moxy.presenter.InjectViewStatePresenter;
import moxy.view.CounterTestView;

public class DelegateLocalPresenterTestView extends CounterTestView {
    @InjectPresenter
    public InjectViewStatePresenter mInjectViewStatePresenter;
}
package moxy.tests;

import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import moxy.MvpDelegate;
import moxy.view.DelegateLocalPresenterTestView;
import moxy.view.TestView;

public class MvpDelegateTest {

    private DelegateLocalPresenterTestView mTestView = new DelegateLocalPresenterTestView();

    private MvpDelegate<? extends TestView> mvpDelegate = new MvpDelegate<>(mTestView);

    @Before
    public void init() {
        mvpDelegate.onCreate(new Bundle());
        mvpDelegate.onAttach();
    }

    @After
    public void reset() {
        mvpDelegate.onDetach();
        mvpDelegate.onDestroy();
    }

    @Test
    public void localPresenterTest() {

    }
}

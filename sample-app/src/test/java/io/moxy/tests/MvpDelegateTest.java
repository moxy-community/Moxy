package io.moxy.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Bundle;

import io.moxy.MvpDelegate;
import io.moxy.view.DelegateLocalPresenterTestView;
import io.moxy.view.TestView;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MvpDelegateTest {

    private DelegateLocalPresenterTestView mTestView = new DelegateLocalPresenterTestView();

    private MvpDelegate<? extends TestView> mvpDelegate = new MvpDelegate<>(mTestView);

    @Before
    public void init() {
        mvpDelegate.onCreate(Mockito.mock(Bundle.class));
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

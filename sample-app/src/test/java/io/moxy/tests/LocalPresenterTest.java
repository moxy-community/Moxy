package moxy.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Bundle;

import java.lang.reflect.Field;

import moxy.MvpDelegate;
import moxy.MvpPresenter;
import moxy.presenter.InjectViewStatePresenter;
import moxy.presenter.NoViewStatePresenter;
import moxy.view.DelegateLocalPresenterTestView;
import moxy.view.TestView;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LocalPresenterTest {

    @Mock
    TestView mTestView;

    DelegateLocalPresenterTestView mDelegateLocalPresenterTestView = new DelegateLocalPresenterTestView();

    DelegateLocalPresenterTestView mDelegateLocalPresenter2TestView = new DelegateLocalPresenterTestView();

    MvpDelegate<? extends TestView> mTestViewMvpDelegate = new MvpDelegate<>(mDelegateLocalPresenterTestView);

    MvpDelegate<? extends TestView> mTestViewMvpDelegate2 = new MvpDelegate<>(mDelegateLocalPresenter2TestView);


    @Before
    public void setup() {
        mTestView = mock(TestView.class);

        mTestViewMvpDelegate.onCreate(null);
        mTestViewMvpDelegate.onAttach();

        mTestViewMvpDelegate2.onCreate(null);
        mTestViewMvpDelegate2.onAttach();
    }

    @After
    public void reset() {
        mTestViewMvpDelegate.onDetach();
        mTestViewMvpDelegate.onDestroy();

        mTestViewMvpDelegate2.onDetach();
        mTestViewMvpDelegate2.onDestroy();
    }

    @Test
    public void checkWithInjectViewState() {
        InjectViewStatePresenter injectViewStatePresenter = new InjectViewStatePresenter();
        injectViewStatePresenter.attachView(mTestView);
        try {
            Field mViewState = MvpPresenter.class.getDeclaredField("mViewState");

            mViewState.setAccessible(true);
            assertTrue("ViewState is null for InjectViewStatePresenter",
                    mViewState.get(injectViewStatePresenter) != null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            assertFalse(e.getLocalizedMessage(), true);
        }
    }

    @Test
    public void checkWithoutViewState() {
        NoViewStatePresenter noViewStatePresenter = new NoViewStatePresenter();
        noViewStatePresenter.attachView(mTestView);
        try {
            Field mViewState = MvpPresenter.class.getDeclaredField("mViewState");

            mViewState.setAccessible(true);
            assertTrue("ViewState is not null for NoViewStatePresenter", mViewState.get(noViewStatePresenter) == null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            assertFalse(e.getLocalizedMessage(), true);
        }
    }

    @Test
    public void checkDelegatePresenter() {
        assertTrue("Presenter is null for delegate", mDelegateLocalPresenterTestView.mInjectViewStatePresenter != null);
    }

    @Test
    public void checkLocalPresenters() {
        assertNotEquals("Local Presenters for two different view is equal",
                mDelegateLocalPresenterTestView.mInjectViewStatePresenter.hashCode(),
                mDelegateLocalPresenter2TestView.mInjectViewStatePresenter.hashCode());
    }

    @Test
    public void checkSaveState() {
        int hashCode = mDelegateLocalPresenterTestView.mInjectViewStatePresenter.hashCode();

        Bundle bundle = new Bundle();

        mTestViewMvpDelegate.onSaveInstanceState(bundle);
        mTestViewMvpDelegate.onDetach();
        mTestViewMvpDelegate.onDestroy();

        mTestViewMvpDelegate.onCreate(bundle);
        mTestViewMvpDelegate.onAttach();

        //TODO: should be passed! Or change test
        //assertTrue("Local presenter has different hashCode after recreate", hashCode == mDelegateLocalPresenterTestView.mInjectViewStatePresenter.hashCode());

        mTestViewMvpDelegate.onDetach();
        mTestViewMvpDelegate.onDestroy();

        mTestViewMvpDelegate.onCreate();
        mTestViewMvpDelegate.onAttach();

        assertFalse("Local presenter has same hashCode after creating new view",
                hashCode == mDelegateLocalPresenterTestView.mInjectViewStatePresenter.hashCode());
    }
}

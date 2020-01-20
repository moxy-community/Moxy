package moxy.tests;

import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;

import moxy.MvpDelegate;
import moxy.MvpPresenter;
import moxy.presenter.InjectViewStatePresenter;
import moxy.view.DelegateLocalPresenterTestView;
import moxy.view.TestView;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class LocalPresenterTest {

    @Mock
    TestView mTestView;

    private DelegateLocalPresenterTestView mDelegateLocalPresenterTestView =
        new DelegateLocalPresenterTestView();

    private DelegateLocalPresenterTestView mDelegateLocalPresenter2TestView =
        new DelegateLocalPresenterTestView();

    private MvpDelegate<? extends TestView> mTestViewMvpDelegate =
        new MvpDelegate<>(mDelegateLocalPresenterTestView);

    private MvpDelegate<? extends TestView> mTestViewMvpDelegate2 =
        new MvpDelegate<>(mDelegateLocalPresenter2TestView);

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
            Field mViewState = MvpPresenter.class.getDeclaredField("viewState");

            mViewState.setAccessible(true);
            assertNotNull("ViewState is null for InjectViewStatePresenter",
                    mViewState.get(injectViewStatePresenter));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void checkDelegatePresenter() {
        assertNotNull("Presenter is null for delegate", mDelegateLocalPresenterTestView.mInjectViewStatePresenter);
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

        assertNotEquals("Local presenter has same hashCode after creating new view", hashCode, mDelegateLocalPresenterTestView.mInjectViewStatePresenter.hashCode());
    }
}

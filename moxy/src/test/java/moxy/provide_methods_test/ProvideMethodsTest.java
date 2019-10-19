package moxy.provide_methods_test;

import android.os.Bundle;
import moxy.MvpDelegate;
import moxy.provide_methods_test.resources.LocalProvidedView;
import moxy.provide_methods_test.resources.TwoLocalProvidedView;
import org.junit.Assert;
import org.junit.Test;

public class ProvideMethodsTest {

    @Test
    public void testLocalIsProvided() {
        LocalProvidedView view = new LocalProvidedView();

        view.delegate = new MvpDelegate<>(view);
        view.delegate.onCreate(new Bundle());

        Assert.assertNotNull(view.oneLocalPresenter);
        Assert.assertSame(view.oneLocalPresenter, view.oneLocalProvidedPresenter);
    }

    @Test
    public void testTwoLocalUseDifferentProvided() {
        TwoLocalProvidedView view = new TwoLocalProvidedView();

        view.delegate = new MvpDelegate<>(view);
        view.delegate.onCreate(new Bundle());

        Assert.assertNotSame(view.oneLocalPresenter, view.secondLocalPresenter);
    }
}

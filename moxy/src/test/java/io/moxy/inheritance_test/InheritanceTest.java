package io.moxy.inheritance_test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Bundle;

import io.moxy.MvpDelegate;
import io.moxy.inheritance_test.resources.ChildViewWithoutInject;
import io.moxy.inheritance_test.resources.SuperViewWithInject;
import io.moxy.inheritance_test.resources.ViewWithoutInject;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class InheritanceTest {

    @Test
    public void testWithoutInject() {
        ViewWithoutInject view = new ViewWithoutInject();

        view.delegate = new MvpDelegate<>(view);

        view.delegate.onCreate(new Bundle());
    }

    @Test
    public void testInjectInInherited() {
        SuperViewWithInject view = new SuperViewWithInject();

        view.delegate = new MvpDelegate<>(view);

        view.delegate.onCreate(new Bundle());

        Assert.assertNotNull(view.presenter);
    }

    @Test
    public void testInjectOnlyInSuper() {
        ChildViewWithoutInject view = new ChildViewWithoutInject();

        view.delegate = new MvpDelegate<>(view);

        view.delegate.onCreate();

        Assert.assertNotNull(view.presenter);
    }
}

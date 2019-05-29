package moxy.inheritance_test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

//import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import moxy.MvpDelegate;
import moxy.inheritance_test.resources.ChildViewWithoutInject;
import moxy.inheritance_test.resources.SuperViewWithInject;
import moxy.inheritance_test.resources.ViewWithoutInject;

@RunWith(AndroidJUnit4.class)
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

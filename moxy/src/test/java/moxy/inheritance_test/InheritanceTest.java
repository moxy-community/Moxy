package moxy.inheritance_test;

import android.os.Bundle;
import moxy.MvpDelegate;
import moxy.inheritance_test.resources.ChildViewWithoutInject;
import moxy.inheritance_test.resources.SuperViewWithInject;
import moxy.inheritance_test.resources.ViewWithoutInject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Requires compilation") // TODO
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

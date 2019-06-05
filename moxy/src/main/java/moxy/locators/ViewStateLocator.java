package moxy.locators;

import moxy.MoxyReflector;
import moxy.MvpView;

public class ViewStateLocator {

    private ViewStateLocator() {
    }

    public static MvpView getViewState(Class<?> presenterClass) {
        return (MvpView) MoxyReflector.getViewState(presenterClass);
    }
}

package moxy.locators;

import moxy.MoxyReflector;

import java.util.List;

public class PresenterBinderLocator {

    private PresenterBinderLocator() {
    }

    public static List<Object> getPresenterBinders(Class<?> delegated) {
        return MoxyReflector.getPresenterBinders(delegated);
    }
}

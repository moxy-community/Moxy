package moxy.locators;

import moxy.MvpProcessor;

import java.util.ArrayList;
import java.util.List;

public class PresenterBinderLocator {

    private PresenterBinderLocator() {
    }

    public static List<Object> getPresenterBinders(Class<?> delegated) {
        //TODO: edit PresenterBinderClassGenerator to generate code, that returns list of super presenter binders
        ArrayList<Object> binders = new ArrayList<>();
        Class<?> currentClass = delegated;
        do {
            Object presenterBinder = locatePresenterBinder(currentClass);
            if (presenterBinder != null) {
                binders.add(presenterBinder);
            }
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null);
        return binders;
    }

    private static Object locatePresenterBinder(Class<?> delegated) {
        try {
            return Class.forName(delegated.getName() + MvpProcessor.PRESENTER_BINDER_SUFFIX).newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}

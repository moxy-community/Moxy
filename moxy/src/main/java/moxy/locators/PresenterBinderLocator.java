package moxy.locators;

import moxy.MvpProcessor;
import moxy.PresenterBinder;

public class PresenterBinderLocator {

  private PresenterBinderLocator() {
  }

  public static <Delegated> PresenterBinder<Delegated> getPresenterBinders(Class<?> delegated) {
    Class<?> currentClass = delegated;
    do {
      PresenterBinder<?> presenterBinder = locatePresenterBinder(currentClass);
      if (presenterBinder != null) {
        //noinspection unchecked
        return (PresenterBinder<Delegated>) presenterBinder;
      }
      currentClass = currentClass.getSuperclass();
    } while (currentClass != null);
    return null;
  }

  private static PresenterBinder<?> locatePresenterBinder(Class<?> delegated) {
    try {
      String className = delegated.getName() + MvpProcessor.PRESENTER_BINDER_SUFFIX;
      return (PresenterBinder<?>) Class.forName(className).newInstance();
    } catch (Exception e) {
      return null;
    }
  }
}

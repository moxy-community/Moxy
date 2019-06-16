package moxy.locators;

import moxy.MvpProcessor;
import moxy.ViewStateProvider;
import moxy.viewstate.MvpViewState;

public class ViewStateLocator {

  private ViewStateLocator() {
  }

  public static MvpViewState getViewState(Class<?> presenterClass) {
    try {
      String className = presenterClass.getName() + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX;
      Class<? extends ViewStateProvider> aClass =
        (Class<? extends ViewStateProvider>) Class.forName(className);
      return aClass.newInstance().getViewState();
    } catch (Exception e) {
      return null;
    }
  }
}

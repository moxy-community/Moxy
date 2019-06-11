package moxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class MoxyReflector {

  private static Map<Class<?>, Object> viewStateProviders;

  private static Map<Class<?>, List<Object>> presenterBinders;

  private static Map<Class<?>, Object> strategies;

  static {
    viewStateProviders = new HashMap<>();
    presenterBinders = new HashMap<>();
  }

  private MoxyReflector() {
  }

  public static Object getViewState(Class<?> presenterClass) {
    return viewStateProviders.get(presenterClass);
  }

  public static List<Object> getPresenterBinders(Class<?> delegated) {
    return presenterBinders.get(delegated);
  }

  public static Object getStrategy(Class strategyClass) {
    return strategies.get(strategyClass);
  }
}

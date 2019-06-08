package moxy.view;

import java.util.HashMap;
import java.util.Map;

public class CounterTestView implements TestView {

  public final Map<String, Integer> counterEvents = new HashMap<>();

  @Override
  public void testEvent() {
    String testEvent = "testEvent";
    if (counterEvents.containsKey(testEvent)) {
      counterEvents.put(testEvent, counterEvents.get(testEvent) + 1);
    } else {
      counterEvents.put(testEvent, 1);
    }
  }
}

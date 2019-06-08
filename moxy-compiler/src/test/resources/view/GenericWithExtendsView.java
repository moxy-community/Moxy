package view;

import java.io.Serializable;
import moxy.MvpView;

public interface GenericWithExtendsView<T extends Serializable> extends MvpView {
  void testEvent(T param);
}

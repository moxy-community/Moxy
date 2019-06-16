package view;

import moxy.MvpView;

public interface ViewStateForGenericView<T> extends MvpView {

  void testEvent(T ter);
}

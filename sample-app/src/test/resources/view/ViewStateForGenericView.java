package view;

import io.moxy.MvpView;

public interface ViewStateForGenericView<T> extends MvpView {

    void testEvent(T ter);
}

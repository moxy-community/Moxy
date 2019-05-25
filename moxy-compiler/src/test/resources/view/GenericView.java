package view;

import io.moxy.MvpView;

public interface GenericView<T> extends MvpView {
	void testEvent(T param);
}

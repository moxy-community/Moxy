package view;

import moxy.MvpView;

public interface GenericView<T> extends MvpView {
	void testEvent(T param);
}

package view;

import io.moxy.MvpView;

import java.io.Serializable;

public interface GenericWithExtendsView<T extends Serializable> extends MvpView {
	void testEvent(T param);
}

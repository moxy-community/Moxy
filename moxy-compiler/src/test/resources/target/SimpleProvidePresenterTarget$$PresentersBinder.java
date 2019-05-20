package target;

import io.moxy.MvpPresenter;
import io.moxy.PresenterBinder;
import io.moxy.presenter.PresenterField;
import io.moxy.presenter.PresenterType;

import java.util.ArrayList;
import java.util.List;

import presenter.EmptyViewPresenter;

public class SimpleProvidePresenterTarget$$PresentersBinder extends PresenterBinder<SimpleProvidePresenterTarget> {
	public List<PresenterField<SimpleProvidePresenterTarget>> getPresenterFields() {
		List<PresenterField<SimpleProvidePresenterTarget>> presenters = new ArrayList<>(1);

		presenters.add(new presenterBinder());

		return presenters;
	}

	public class presenterBinder extends PresenterField<SimpleProvidePresenterTarget> {
		public presenterBinder() {
			super("presenter", PresenterType.LOCAL, null, EmptyViewPresenter.class);
		}

		@Override
		public void bind(SimpleProvidePresenterTarget target, MvpPresenter presenter) {
			target.presenter = (EmptyViewPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(SimpleProvidePresenterTarget delegated) {
			return delegated.providePresenter();
		}
	}

}
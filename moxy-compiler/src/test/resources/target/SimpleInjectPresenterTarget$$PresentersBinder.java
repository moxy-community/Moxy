package target;

import io.moxy.MvpPresenter;
import io.moxy.PresenterBinder;
import io.moxy.presenter.PresenterField;

import java.util.ArrayList;
import java.util.List;

import presenter.EmptyViewPresenter;

public class SimpleInjectPresenterTarget$$PresentersBinder extends PresenterBinder<SimpleInjectPresenterTarget> {
	public List<PresenterField<SimpleInjectPresenterTarget>> getPresenterFields() {
		List<PresenterField<SimpleInjectPresenterTarget>> presenters = new ArrayList<>(1);

		presenters.add(new PresenterBinder());

		return presenters;
	}

	public class PresenterBinder extends PresenterField<SimpleInjectPresenterTarget> {
		public PresenterBinder() {
			super("presenter", null, EmptyViewPresenter.class);
		}

		@Override
		public void bind(SimpleInjectPresenterTarget target, MvpPresenter presenter) {
			target.presenter = (EmptyViewPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(SimpleInjectPresenterTarget delegated) {
			return new EmptyViewPresenter();
		}
	}

}
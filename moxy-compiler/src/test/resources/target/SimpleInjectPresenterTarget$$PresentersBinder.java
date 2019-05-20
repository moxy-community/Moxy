package target;

import io.moxy.MvpPresenter;
import io.moxy.PresenterBinder;
import io.moxy.presenter.PresenterField;
import io.moxy.presenter.PresenterType;

import java.util.ArrayList;
import java.util.List;

import presenter.EmptyViewPresenter;

public class SimpleInjectPresenterTarget$$PresentersBinder extends PresenterBinder<SimpleInjectPresenterTarget> {
	public List<PresenterField<SimpleInjectPresenterTarget>> getPresenterFields() {
		List<PresenterField<SimpleInjectPresenterTarget>> presenters = new ArrayList<>(1);

		presenters.add(new presenterBinder());

		return presenters;
	}

	public class presenterBinder extends PresenterField<SimpleInjectPresenterTarget> {
		public presenterBinder() {
			super("presenter", PresenterType.LOCAL, null, EmptyViewPresenter.class);
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
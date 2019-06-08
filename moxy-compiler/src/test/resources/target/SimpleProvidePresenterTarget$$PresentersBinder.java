package target;

import java.util.ArrayList;
import java.util.List;
import moxy.MvpPresenter;
import moxy.PresenterBinder;
import moxy.presenter.PresenterField;
import presenter.EmptyViewPresenter;

public class SimpleProvidePresenterTarget$$PresentersBinder
    extends PresenterBinder<SimpleProvidePresenterTarget> {
  public List<PresenterField<SimpleProvidePresenterTarget>> getPresenterFields() {
    List<PresenterField<SimpleProvidePresenterTarget>> presenters = new ArrayList<>(1);

    presenters.add(new PresenterBinder());

    return presenters;
  }

  public class PresenterBinder extends PresenterField<SimpleProvidePresenterTarget> {
    public PresenterBinder() {
      super("presenter", null, EmptyViewPresenter.class);
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
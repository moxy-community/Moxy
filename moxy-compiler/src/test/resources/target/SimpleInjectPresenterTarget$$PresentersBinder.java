package target;

import java.util.ArrayList;
import java.util.List;
import moxy.MvpPresenter;
import moxy.PresenterBinder;
import moxy.presenter.PresenterField;
import presenter.EmptyViewPresenter;

public class SimpleInjectPresenterTarget$$PresentersBinder
  extends PresenterBinder<SimpleInjectPresenterTarget> {
  public List<PresenterField<? super SimpleInjectPresenterTarget>> getPresenterFields() {
    List<PresenterField<? super SimpleInjectPresenterTarget>> presenters = new ArrayList<>(1);

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
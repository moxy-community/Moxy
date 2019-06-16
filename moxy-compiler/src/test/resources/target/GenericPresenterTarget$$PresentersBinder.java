package target;

import java.util.ArrayList;
import java.util.List;
import moxy.MvpPresenter;
import moxy.PresenterBinder;
import moxy.presenter.PresenterField;
import presenter.GenericPresenter;

public class GenericPresenterTarget$$PresentersBinder
  extends PresenterBinder<GenericPresenterTarget> {
  public List<PresenterField<? super GenericPresenterTarget>> getPresenterFields() {
    List<PresenterField<? super GenericPresenterTarget>> presenters = new ArrayList<>(1);

    presenters.add(new PresenterBinder());

    return presenters;
  }

  public class PresenterBinder extends PresenterField<GenericPresenterTarget> {
    public PresenterBinder() {
      super("presenter", null, GenericPresenter.class);
    }

    @Override
    public void bind(GenericPresenterTarget target, MvpPresenter presenter) {
      target.presenter = (GenericPresenter) presenter;
    }

    @Override
    public MvpPresenter<?> providePresenter(GenericPresenterTarget delegated) {
      return delegated.providePresenter();
    }
  }
}
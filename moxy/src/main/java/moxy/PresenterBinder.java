package moxy;

import java.util.List;

import moxy.presenter.PresenterField;

public abstract class PresenterBinder<PresentersContainer> {

    public abstract List<PresenterField<PresentersContainer>> getPresenterFields();
}

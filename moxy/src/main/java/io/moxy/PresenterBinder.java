package io.moxy;

import java.util.List;

import io.moxy.presenter.PresenterField;

public abstract class PresenterBinder<PresentersContainer> {

    public abstract List<PresenterField<PresentersContainer>> getPresenterFields();
}

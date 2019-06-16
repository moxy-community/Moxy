package target;

import moxy.presenter.InjectPresenter;
import presenter.EmptyViewPresenter;
import view.EmptyView;

public class SimpleInjectPresenterTarget implements EmptyView {
    @InjectPresenter
    EmptyViewPresenter presenter;
}

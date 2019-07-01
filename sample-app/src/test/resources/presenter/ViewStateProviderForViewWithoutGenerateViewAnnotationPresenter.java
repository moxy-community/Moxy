package presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import view.WithoutGenerateViewAnnotationView;

@InjectViewState
public class ViewStateProviderForViewWithoutGenerateViewAnnotationPresenter
    extends MvpPresenter<WithoutGenerateViewAnnotationView> {

}

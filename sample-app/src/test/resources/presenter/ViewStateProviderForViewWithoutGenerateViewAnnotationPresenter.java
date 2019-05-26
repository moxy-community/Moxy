package presenter;

import io.moxy.InjectViewState;
import io.moxy.MvpPresenter;
import view.WithoutGenerateViewAnnotationView;


@InjectViewState
public class ViewStateProviderForViewWithoutGenerateViewAnnotationPresenter
        extends MvpPresenter<WithoutGenerateViewAnnotationView> {

}

package presenter;

import io.moxy.MvpPresenter;
import io.moxy.MvpView;

public class PresenterWithoutEmptyConstructor<V extends MvpView> extends MvpPresenter<V> {

    public PresenterWithoutEmptyConstructor(String s) {/*do nothing*/}

    private PresenterWithoutEmptyConstructor() {/*do nothing*/}
}

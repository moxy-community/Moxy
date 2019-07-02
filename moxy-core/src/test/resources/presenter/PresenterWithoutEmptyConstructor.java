package presenter;

import moxy.MvpPresenter;
import moxy.MvpView;

public class PresenterWithoutEmptyConstructor<V extends MvpView> extends MvpPresenter<V> {

    public PresenterWithoutEmptyConstructor(String s) {/*do nothing*/}

    private PresenterWithoutEmptyConstructor() {/*do nothing*/}
}

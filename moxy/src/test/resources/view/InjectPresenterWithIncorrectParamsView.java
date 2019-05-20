package view;

import io.moxy.factory.MockPresenterFactory2;
import io.moxy.presenter.InjectPresenter;
import io.moxy.presenter.PresenterType;
import io.moxy.presenter.TestViewPresenter;
import io.moxy.view.TestView;
import params.Params1;

public class InjectPresenterWithIncorrectParamsView implements TestView, Params1 {

    @InjectPresenter(factory = MockPresenterFactory2.class, presenterId = "Test", type = PresenterType.LOCAL)
    public TestViewPresenter mPresenter;

    @Override
    public void testEvent() {

    }

    @Override
    public String mockParams1(final String presenterId) {
        return null;
    }
}

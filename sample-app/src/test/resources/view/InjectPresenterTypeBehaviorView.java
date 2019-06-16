package view;

import moxy.MvpView;
import moxy.factory.MockPresenterFactory;
import moxy.presenter.InjectPresenter;
import moxy.presenter.PresenterType;
import presenter.PositiveParamsViewPresenter;

public class InjectPresenterTypeBehaviorView implements MvpView {

    @InjectPresenter(tag = "", type = PresenterType.LOCAL)
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mTagLocalPresenter;

    @InjectPresenter(factory = MockPresenterFactory.class, type = PresenterType.LOCAL)
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mFactoryLocalPresenter;

    @InjectPresenter(presenterId = "", type = PresenterType.LOCAL)
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mPresenterIdLocalPresenter;

    @InjectPresenter(tag = "")
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mTagLocalPresenter2;

    @InjectPresenter(factory = MockPresenterFactory.class)
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mFactoryLocalPresenter2;

    @InjectPresenter(presenterId = "")
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView>
        mPresenterIdLocalPresenter2;

    @InjectPresenter(tag = "", factory = MockPresenterFactory.class)
    private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mFactoryTagPresenter;
}

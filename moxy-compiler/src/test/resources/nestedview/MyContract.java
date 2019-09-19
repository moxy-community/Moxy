import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface MyContract {

    @StateStrategyType(OneExecutionStateStrategy.class)
    public interface View extends MvpView {
        void someMethod();
    }
}

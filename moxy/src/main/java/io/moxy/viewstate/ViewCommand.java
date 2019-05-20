package io.moxy.viewstate;

import io.moxy.MvpView;
import io.moxy.viewstate.strategy.StateStrategy;


public abstract class ViewCommand<View extends MvpView> {

    private final String tag;

    private final Class<? extends StateStrategy> stateStrategyType;

    protected ViewCommand(String tag, Class<? extends StateStrategy> stateStrategyType) {
        this.tag = tag;
        this.stateStrategyType = stateStrategyType;
    }

    public abstract void apply(View view);

    public String getTag() {
        return tag;
    }

    public Class<? extends StateStrategy> getStrategyType() {
        return stateStrategyType;
    }
}

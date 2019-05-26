package view;

import io.moxy.MvpView;
import params.Params1;
import params.Params2;

public class SeveralParamsView implements MvpView, Params1, Params2 {

    @Override
    public String mockParams1(final String presenterId) {
        return null;
    }

    @Override
    public String mockParams2(final String presenterId) {
        return null;
    }
}

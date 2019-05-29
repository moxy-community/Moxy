package params;

import moxy.ParamsProvider;
import moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface Params2 {
	String mockParams2(String presenterId);
}

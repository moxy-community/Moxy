package params;

import moxy.ParamsProvider;
import moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface Params1 {
	String mockParams1(String presenterId);
}

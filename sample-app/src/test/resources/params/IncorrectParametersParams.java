package params;

import moxy.ParamsProvider;
import moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface IncorrectParametersParams {
	void method1(Integer i);
}

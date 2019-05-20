package params;

import io.moxy.ParamsProvider;
import io.moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface IncorrectParametersParams {
	void method1(Integer i);
}

package params;

import io.moxy.ParamsProvider;
import io.moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface IncorrectCountOfParametersParams {
	void method1(String s1, String s2);
}

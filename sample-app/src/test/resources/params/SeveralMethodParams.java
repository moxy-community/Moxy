package params;

import io.moxy.ParamsProvider;
import io.moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface SeveralMethodParams {
	void method1();

	void method2();
}

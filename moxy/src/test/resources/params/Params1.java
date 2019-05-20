package params;

import io.moxy.ParamsProvider;
import io.moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface Params1 {
	String mockParams1(String presenterId);
}

package params;

import io.moxy.ParamsProvider;
import io.moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface Params2 {
	String mockParams2(String presenterId);
}

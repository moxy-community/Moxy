package params;

import moxy.ParamsProvider;
import moxy.factory.MockPresenterFactory;

@ParamsProvider(MockPresenterFactory.class)
public interface SeveralMethodParams {
  void method1();

  void method2();
}

package io.student.rcc.jupiter.extension;

import io.student.rcc.model.TestData;
import org.junit.jupiter.api.extension.*;

import static io.student.rcc.jupiter.extension.TestMethodContextExtension.context;

public class TestDataExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE =
      ExtensionContext.Namespace.create(TestDataExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {
    TestData testData = new TestData(null, null, null, null, null);
    context.getStore(NAMESPACE).put(context.getUniqueId(), testData);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    context.getStore(NAMESPACE).remove(context.getUniqueId());
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(TestData.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
                                 ExtensionContext extensionContext) throws ParameterResolutionException {
    return createdTestData();
  }

  public static TestData createdTestData() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
        .get(methodContext.getUniqueId(), TestData.class);
  }

  public static void updateTestData(TestData testData) {
    context().getStore(NAMESPACE).put(context().getUniqueId(), testData);
  }
}

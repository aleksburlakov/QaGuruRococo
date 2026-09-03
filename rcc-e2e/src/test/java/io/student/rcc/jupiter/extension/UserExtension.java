package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.TestData;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.UsersDbClient;
import io.student.rcc.utils.TestDataGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
  private static final String DEFAULT_PASSWORD = "12345";

  private final UsersClient usersClient = new UsersDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        annotation -> {
          String username = annotation.username().isEmpty()
              ? TestDataGenerator.generateRandomLogin()
              : annotation.username();
          String password = annotation.password().isEmpty()
              ? DEFAULT_PASSWORD
              : annotation.password();

          UserJson user = usersClient.createUser(username, password);
          context.getStore(NAMESPACE).put(context.getUniqueId(), user);

          TestData createdTestData = TestDataExtension.createdTestData();
          TestData newTestData =
              new TestData(user, password, createdTestData.artist(), createdTestData.museum(), createdTestData.painting());
          TestDataExtension.updateTestData(newTestData);
        }
    );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(UserJson.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    TestData createdTestData = TestDataExtension.createdTestData();
    UserJson createdUser = createdTestData.user();
    if (createdUser != null) {
      usersClient.removeUser(createdUser);
    }
  }
}

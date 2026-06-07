package io.student.rcc.jupiter.extension;

import static io.student.rcc.utils.TestDataGenerator.generateRandomFirstname;
import static io.student.rcc.utils.TestDataGenerator.generateRandomLastname;
import static io.student.rcc.utils.TestDataGenerator.generateRandomLogin;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.UsersDbClient;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
  private static final String DEFAULT_USER_PASSWORD = "12345";

  private final UsersClient usersClient = new UsersDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        annotation -> {
          UserJson user = new UserJson(
              null,
              generateRandomLogin(),
              DEFAULT_USER_PASSWORD,
              generateRandomFirstname(),
              generateRandomLastname(),
              null
          );
          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              usersClient.createUser(user)
          );
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
}

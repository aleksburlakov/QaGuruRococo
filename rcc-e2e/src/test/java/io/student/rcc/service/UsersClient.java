package io.student.rcc.service;

import io.student.rcc.model.UserJson;
import io.student.rcc.service.impl.UsersApiClient;
import io.student.rcc.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UsersClient {

  @Nonnull
  static UsersClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new UsersApiClient()
        : new UsersDbClient();
  }

  @Nonnull
  UserJson createUser(@Nonnull String username, @Nonnull String password);

  @Nonnull
  UserJson updateUser(@Nonnull UserJson userJson);

  @Nonnull
  Optional<UserJson> findUserById(@Nonnull UUID id);

  @Nonnull
  Optional<UserJson> findUserByUsername(@Nonnull String username);

  @Nonnull
  List<UserJson> findAll();

  void removeUser(@Nonnull UserJson userJson);
}

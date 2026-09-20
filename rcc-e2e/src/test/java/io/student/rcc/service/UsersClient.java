package io.student.rcc.service;

import io.student.rcc.model.UserJson;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

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

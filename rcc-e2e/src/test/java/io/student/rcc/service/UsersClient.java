package io.student.rcc.service;

import io.student.rcc.model.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

  UserJson createUser(String username, String password);

  UserJson updateUser(UserJson userJson);

  Optional<UserJson> findUserById(UUID id);

  Optional<UserJson> findUserByUsername(String username);

  List<UserJson> findAll();

  void removeUser(UserJson userJson);
}

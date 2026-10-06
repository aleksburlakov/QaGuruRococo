package io.student.rcc.service.impl;

import io.qameta.allure.Step;
import io.student.rcc.api.AuthApiClient;
import io.student.rcc.api.UserApiClient;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.UsersClient;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ParametersAreNonnullByDefault
public class UsersApiClient implements UsersClient {

  private final AuthApiClient authApiClient;
  private final UserApiClient userApiClient;

  public UsersApiClient() {
    this.authApiClient = new AuthApiClient();
    this.userApiClient = new UserApiClient();
  }

  @Override
  @Step("Создать пользователя с username = {username} и password = {password} через API")
  public @NonNull UserJson createUser(@NonNull String username, @NonNull String password) {
    Response authResponse = authApiClient.registerUser(username, password);
    Assertions.assertTrue(authResponse.isSuccessful(), "Ошибка при создании клиента в auth");
    // Пока не реализовано в UserController
    return null;
  }

  @Override
  public @NonNull UserJson updateUser(@NonNull UserJson userJson) {
    return null;
  }

  @Override
  public @NonNull Optional<UserJson> findUserById(@NonNull UUID id) {
    return Optional.empty();
  }

  @Override
  public @NonNull Optional<UserJson> findUserByUsername(@NonNull String username) {
    return Optional.empty();
  }

  @Override
  public @NonNull List<UserJson> findAll() {
    return List.of();
  }

  @Override
  public void removeUser(@NonNull UserJson userJson) {

  }
}

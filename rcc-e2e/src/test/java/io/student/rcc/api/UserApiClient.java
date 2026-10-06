package io.student.rcc.api;

import io.student.rcc.api.core.RestClient;
import io.student.rcc.model.UserJson;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

public class UserApiClient extends RestClient {

  private final UserApi userApi;

  public UserApiClient() {
    super(CFG.apiUrl());
    this.userApi = create(UserApi.class);
  }

  @Nonnull
  public Response<UserJson> registerUser(String username, String password) {
    Response<UserJson> response;
    try {
      response = userApi.getUser().execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return response;
  }
}

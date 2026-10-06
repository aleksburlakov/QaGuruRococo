package io.student.rcc.api;

import io.student.rcc.api.core.RestClient;
import io.student.rcc.api.core.ThreadSafeCookieStore;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

public class AuthApiClient extends RestClient {

  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl());
    this.authApi = create(AuthApi.class);
  }

  @Nonnull
  public Response<Void> registerUser(String username, String password) {
    try {
      authApi.requestRegisterForm().execute();
      return authApi.register(
          username,
          password,
          password,
          ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

package io.student.rcc.api;

import io.student.rcc.model.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserApi {

  @GET("/api/user")
  Call<UserJson> getUser();
}

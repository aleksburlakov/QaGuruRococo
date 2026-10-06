package io.student.rcc.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthApi {

  @GET("/register")
  Call<ResponseBody> requestRegisterForm();

  @FormUrlEncoded
  @POST("/register")
  Call<Void> register(
      @Field("_csrf") String csrf,
      @Field("username") String username,
      @Field("password") String password,
      @Field("passwordSubmit") String passwordSubmit
  );
}

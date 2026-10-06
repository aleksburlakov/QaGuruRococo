package io.student.rcc.api;

import io.student.rcc.model.MuseumJson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface MuseumApi {

  @POST("/api/museum")
  @Nonnull
  Call<MuseumJson> createMuseum(@Body MuseumJson museumJson);

  @PATCH("/api/museum")
  @Nonnull
  Call<MuseumJson> updateMuseum(@Body MuseumJson museumJson);

  @GET("/api/museum/{id}")
  @Nonnull
  Call<MuseumJson> getMuseumById(@Path("id") String id);

  @GET("/api/museum")
  @Nonnull
  Call<List<MuseumJson>> getAllMuseums(
      @Query("page") int page,
      @Query("size") int size
  );
}

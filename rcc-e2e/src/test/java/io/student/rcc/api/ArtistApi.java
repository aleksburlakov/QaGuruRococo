package io.student.rcc.api;

import io.student.rcc.model.ArtistJson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface ArtistApi {

  @POST("/api/artist")
  @Nonnull
  Call<ArtistJson> createArtist(@Body ArtistJson artistJson);

  @PATCH("/api/artist")
  @Nonnull
  Call<ArtistJson> updateArtist(@Body ArtistJson artistJson);

  @GET("/api/artist/{id}")
  @Nonnull
  Call<ArtistJson> getArtistById(@Path("id") String id);

  @GET("/api/artist")
  @Nonnull
  Call<List<ArtistJson>> getAllArtists(
      @Query("page") int page,
      @Query("size") int size
  );
}

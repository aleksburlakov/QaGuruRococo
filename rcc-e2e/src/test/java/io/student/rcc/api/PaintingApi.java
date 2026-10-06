package io.student.rcc.api;

import io.student.rcc.model.PaintingJson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface PaintingApi {

  @POST("/api/painting")
  @Nonnull
  Call<PaintingJson> createPainting(@Body PaintingJson paintingJson);

  @PATCH("/api/painting")
  @Nonnull
  Call<PaintingJson> updatePainting(@Body PaintingJson paintingJson);

  @GET("/api/painting/{id}")
  @Nonnull
  Call<PaintingJson> getPaintingById(@Path("id") String id);

  @GET("/api/painting")
  @Nonnull
  Call<Response<List<PaintingJson>>> getAllPaintings(
      @Query("page") int page,
      @Query("size") int size
  );

}

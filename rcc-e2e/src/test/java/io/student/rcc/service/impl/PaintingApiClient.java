package io.student.rcc.service.impl;

import io.qameta.allure.Step;
import io.student.rcc.api.PaintingApi;
import io.student.rcc.api.core.RestClient;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.service.PaintingClient;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ParametersAreNonnullByDefault
public class PaintingApiClient extends RestClient implements PaintingClient {

  private final PaintingApi paintingApi;

  public PaintingApiClient() {
    super(CFG.apiUrl());
    this.paintingApi = create(PaintingApi.class);
  }

  @Override
  @Step("Create painting using internal REST API")
  public @NonNull PaintingJson createPainting(PaintingJson painting) {
    final Response<PaintingJson> response;
    try {
      response = paintingApi.createPainting(painting).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  @Override
  @Step("Edit painting using internal REST API")
  public @NonNull PaintingJson updatePainting(PaintingJson painting) {
    final Response<PaintingJson> response;
    try {
      response = paintingApi.updatePainting(painting).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Step("Get painting using internal REST API by id: {id}")
  public @NonNull Optional<PaintingJson> findPaintingById(UUID id) {
    final Response<PaintingJson> response;
    try {
      response = paintingApi.getPaintingById(id.toString())
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Optional.ofNullable(response.body());
  }

  @Override
  public @NonNull Optional<PaintingJson> findPaintingByTitle(String title) {
    return Optional.empty();
  }

  @Override
  @Step("Get all paintings using internal REST API")
  public @NonNull List<PaintingJson> findAllPaintings() {
    return List.of();
  }

  @Override
  public void removePainting(PaintingJson paintingJson) {

  }
}

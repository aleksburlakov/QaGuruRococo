package io.student.rcc.service.impl;

import io.qameta.allure.Step;
import io.student.rcc.api.ArtistApi;
import io.student.rcc.api.core.RestClient;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.service.ArtistClient;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArtistApiClient extends RestClient implements ArtistClient {

  private final ArtistApi artistApi;

  public ArtistApiClient() {
    super(CFG.apiUrl());
    this.artistApi = create(ArtistApi.class);
  }

  @Override
  @Step("Create artist using internal REST API")
  public @Nullable ArtistJson createArtist(@NonNull ArtistJson artist) {
    final Response<ArtistJson> response;
    try {
      response = artistApi.createArtist(artist).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  @Override
  @Step("Edit artist using internal REST API")
  public @Nullable ArtistJson updateArtist(@NonNull ArtistJson artist) {
    final Response<ArtistJson> response;
    try {
      response = artistApi.updateArtist(artist).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Step("Get artist using internal REST API by id: {id}")
  public @NonNull Optional<ArtistJson> findArtistById(@NonNull UUID id) {
    final Response<ArtistJson> response;
    try {
      response = artistApi.getArtistById(id.toString())
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Optional.ofNullable(response.body());
  }

  @Override
  public @NonNull Optional<ArtistJson> findArtistByName(@NonNull String name) {
    return Optional.empty();
  }

  @Override
  @Step("Get all artists using internal REST API")
  public @Nonnull List<ArtistJson> findAllArtists() {
    final Response<List<ArtistJson>> response;
    try {
      response = artistApi.getAllArtists(0, 0)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body() != null
        ? response.body()
        : Collections.emptyList();
  }

  @Override
  public void removeArtist(@NonNull ArtistJson artistJson) {

  }

  @Override
  public void removeAllArtists() {

  }
}

package io.student.rcc.service.impl;

import io.qameta.allure.Step;
import io.student.rcc.api.MuseumApi;
import io.student.rcc.api.core.RestClient;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.service.MuseumClient;
import org.jspecify.annotations.NonNull;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MuseumApiClient extends RestClient implements MuseumClient {

  private final MuseumApi museumApi;

  public MuseumApiClient() {
    super(CFG.apiUrl());
    this.museumApi = create(MuseumApi.class);
  }

  @Override
  @Step("Create museum using internal REST API")
  public @NonNull MuseumJson createMuseum(@NonNull MuseumJson museum) {
    final Response<MuseumJson> response;
    try {
      response = museumApi.createMuseum(museum).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  @Override
  @Step("Edit museum using internal REST API")
  public @NonNull MuseumJson updateMuseum(@NonNull MuseumJson museum) {
    final Response<MuseumJson> response;
    try {
      response = museumApi.updateMuseum(museum).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Step("Get museum using internal REST API by id: {id}")
  public @NonNull Optional<MuseumJson> findMuseumById(@NonNull UUID id) {
    final Response<MuseumJson> response;
    try {
      response = museumApi.getMuseumById(id.toString())
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Optional.ofNullable(response.body());
  }

  @Override
  public @NonNull Optional<MuseumJson> findMuseumByTitle(@NonNull String title) {
    return Optional.empty();
  }

  @Override
  @Step("Get all museums using internal REST API")
  public @NonNull List<MuseumJson> findAllMuseums() {
    final Response<List<MuseumJson>> response;
    try {
      response = museumApi.getAllMuseums(0, 0)
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
  public void removeMuseum(@NonNull MuseumJson museum) {

  }

  @Override
  public void removeAllMuseums() {

  }
}

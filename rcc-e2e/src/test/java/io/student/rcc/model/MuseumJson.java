package io.student.rcc.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.MuseumEntity;

import java.util.UUID;

public record MuseumJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("title")
    String title,
    @JsonProperty("description")
    String description,
    @JsonProperty("city")
    String city,
    @JsonProperty("photo")
    String photo,
    @JsonProperty("country")
    CountryJson country) {

  public static MuseumJson fromEntity(MuseumEntity entity) {
    if (entity == null) {
      return null;
    }
    return new MuseumJson(
        entity.getId(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getCity(),
        entity.getPhoto() != null ? new String(entity.getPhoto()) : null,
        CountryJson.fromEntity(entity.getCountry())
    );
  }
}

package io.student.rcc.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.CountryEntity;

import java.util.UUID;

public record CountryJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("name")
    String name
) {
  public static CountryJson fromEntity(CountryEntity entity) {
    return new CountryJson(
        entity.getId(),
        entity.getName()
    );
  }
}

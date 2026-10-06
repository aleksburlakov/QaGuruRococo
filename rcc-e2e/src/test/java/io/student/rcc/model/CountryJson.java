package io.student.rcc.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.CountryEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public record CountryJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("name")
    String name
) {
  @Nullable
  public static CountryJson fromEntity(@Nullable CountryEntity entity) {
    if (entity == null) {
      return null;
    }
    return new CountryJson(
        entity.getId(),
        entity.getName()
    );
  }
}

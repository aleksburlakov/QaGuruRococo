package io.student.rcc.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.ArtistEntity;

import java.util.Base64;
import java.util.UUID;

public record ArtistJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("biography") String biography,
        @JsonProperty("photo") String photo
) {
    public static ArtistJson fromEntity(ArtistEntity entity) {
        return new ArtistJson(
                entity.getId(),
                entity.getName(),
                entity.getBiography(),
                entity.getPhoto() != null ? Base64.getEncoder().encodeToString(entity.getPhoto()) : null
        );
    }
}

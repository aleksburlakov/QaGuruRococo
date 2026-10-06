package io.student.rcc.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.PaintingEntity;
import org.junit.jupiter.params.shadow.de.siegmar.fastcsv.util.Nullable;

import java.util.Arrays;
import java.util.UUID;

public record PaintingJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("title") String title,
    @JsonProperty("description") String description,
    @JsonProperty("artist") ArtistJson artist,
    @JsonProperty("museum") MuseumJson museum,
    @JsonProperty("content") String content) {

  @Nullable
  public static PaintingJson fromEntity(@Nullable PaintingEntity entity) {
    if (entity == null) {
      return null;
    }
    return new PaintingJson(
        entity.getId(),
        entity.getTitle(),
        entity.getDescription(),
        ArtistJson.fromEntity(entity.getArtist()),
        MuseumJson.fromEntity(entity.getMuseum()),
        entity.getContent() != null ? Arrays.toString(entity.getContent()) : null
    );
  }
}

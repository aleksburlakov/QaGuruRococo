package io.student.rcc.service;

import io.student.rcc.model.PaintingJson;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaintingClient {
  @Nonnull
  PaintingJson createPainting(@Nonnull PaintingJson painting);

  @Nonnull
  PaintingJson updatePainting(@Nonnull PaintingJson painting);

  @Nonnull
  Optional<PaintingJson> findPaintingById(@Nonnull UUID id);

  @Nonnull
  Optional<PaintingJson> findPaintingByTitle(@Nonnull String title);

  @Nonnull
  List<PaintingJson> findAllPaintings();

  void removePainting(@Nonnull PaintingJson paintingJson);
}

package io.student.rcc.service;

import io.student.rcc.model.PaintingJson;
import io.student.rcc.service.impl.PaintingApiClient;
import io.student.rcc.service.impl.PaintingDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface PaintingClient {

  @Nonnull
  static PaintingClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new PaintingApiClient()
        : new PaintingDbClient();
  }

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

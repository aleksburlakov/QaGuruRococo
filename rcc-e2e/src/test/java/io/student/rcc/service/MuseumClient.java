package io.student.rcc.service;

import io.student.rcc.model.MuseumJson;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MuseumClient {

  @Nonnull
  MuseumJson createMuseum(@Nonnull MuseumJson museum);

  @Nonnull
  MuseumJson updateMuseum(@Nonnull MuseumJson museum);

  @Nonnull
  Optional<MuseumJson> findMuseumById(@Nonnull UUID id);

  @Nonnull
  Optional<MuseumJson> findMuseumByTitle(@Nonnull String title);

  @Nonnull
  List<MuseumJson> findAllMuseums();

  void removeMuseum(@Nonnull MuseumJson museum);

  void removeAllMuseums();

}

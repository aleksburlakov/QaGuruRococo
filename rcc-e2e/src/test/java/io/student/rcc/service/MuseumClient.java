package io.student.rcc.service;

import io.student.rcc.model.MuseumJson;
import io.student.rcc.service.impl.MuseumApiClient;
import io.student.rcc.service.impl.MuseumDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface MuseumClient {

  @Nonnull
  static MuseumClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new MuseumApiClient()
        : new MuseumDbClient();
  }

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

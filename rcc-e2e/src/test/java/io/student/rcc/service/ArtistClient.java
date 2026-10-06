package io.student.rcc.service;

import io.student.rcc.model.ArtistJson;
import io.student.rcc.service.impl.ArtistApiClient;
import io.student.rcc.service.impl.ArtistDbClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface ArtistClient {

  @Nonnull
  static ArtistClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new ArtistApiClient()
        : new ArtistDbClient();
  }

  @Nullable
  ArtistJson createArtist(@Nonnull ArtistJson artist);

  @Nullable
  ArtistJson updateArtist(@Nonnull ArtistJson artist);

  @Nonnull
  Optional<ArtistJson> findArtistById(@Nonnull UUID id);

  @Nonnull
  Optional<ArtistJson> findArtistByName(@Nonnull String name);

  @Nonnull
  List<ArtistJson> findAllArtists();

  void removeArtist(@Nonnull ArtistJson artistJson);

  void removeAllArtists();
}

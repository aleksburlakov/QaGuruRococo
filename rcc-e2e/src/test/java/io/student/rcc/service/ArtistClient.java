package io.student.rcc.service;

import io.student.rcc.model.ArtistJson;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistClient {

  @Nonnull
  ArtistJson createArtist(@Nonnull ArtistJson artist);

  @Nonnull
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

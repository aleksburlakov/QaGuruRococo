package io.student.rcc.data.repository;

import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.repository.impl.ArtistRepositoryHibernate;
import io.student.rcc.data.repository.impl.ArtistRepositoryJdbc;
import io.student.rcc.data.repository.impl.ArtistRepositorySpringJdbc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository {

  static ArtistRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new ArtistRepositoryJdbc();
      case "spring-jdbc" -> new ArtistRepositorySpringJdbc();
      default -> new ArtistRepositoryHibernate();
    };
  }

  @Nonnull ArtistEntity create(@Nonnull ArtistEntity artist);

  @Nonnull ArtistEntity update(@Nonnull ArtistEntity artist);

  @Nonnull Optional<ArtistEntity> findById(@Nonnull UUID id);

  @Nonnull Optional<ArtistEntity> findByName(@Nonnull String name);

  @Nonnull List<ArtistEntity> findAll();

  void remove(@Nonnull ArtistEntity artist);

  void removeAll();
}

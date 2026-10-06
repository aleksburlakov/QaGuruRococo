package io.student.rcc.data.repository;

import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.repository.impl.MuseumRepositoryHibernate;
import io.student.rcc.data.repository.impl.MuseumRepositoryJdbc;
import io.student.rcc.data.repository.impl.MuseumRepositorySpringJdbc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MuseumRepository {
  static MuseumRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new MuseumRepositoryJdbc();
      case "spring-jdbc" -> new MuseumRepositorySpringJdbc();
      default -> new MuseumRepositoryHibernate();
    };
  }

  @Nonnull MuseumEntity create(@Nonnull MuseumEntity museum);

  @Nonnull MuseumEntity update(@Nonnull MuseumEntity museum);

  @Nonnull Optional<MuseumEntity> findById(@Nonnull UUID id);

  @Nonnull Optional<MuseumEntity> findByTitle(@Nonnull String title);

  @Nonnull List<MuseumEntity> findAll();

  void remove(@Nonnull MuseumEntity museum);

  void removeAll();
}

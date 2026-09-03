package io.student.rcc.data.repository;

import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.repository.impl.ArtistRepositoryHibernate;
import io.student.rcc.data.repository.impl.ArtistRepositoryJdbc;
import io.student.rcc.data.repository.impl.ArtistRepositorySpringJdbc;

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

  ArtistEntity create(ArtistEntity artist);

  ArtistEntity update(ArtistEntity artist);

  Optional<ArtistEntity> findById(UUID id);

  Optional<ArtistEntity> findByName(String name);

  List<ArtistEntity> findAll();

  void remove(ArtistEntity artist);

  void removeAll();
}

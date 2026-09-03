package io.student.rcc.data.repository;

import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.repository.impl.MuseumRepositoryHibernate;
import io.student.rcc.data.repository.impl.MuseumRepositoryJdbc;
import io.student.rcc.data.repository.impl.MuseumRepositorySpringJdbc;

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

  MuseumEntity create(MuseumEntity museum);

  MuseumEntity update(MuseumEntity museum);

  Optional<MuseumEntity> findById(UUID id);

  Optional<MuseumEntity> findByTitle(String title);

  List<MuseumEntity> findAll();

  void remove(MuseumEntity museum);

  void removeAll();
}

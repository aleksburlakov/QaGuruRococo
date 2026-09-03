package io.student.rcc.data.repository;

import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.repository.impl.PaintingRepositoryHibernate;
import io.student.rcc.data.repository.impl.PaintingRepositoryJdbc;
import io.student.rcc.data.repository.impl.PaintingRepositorySpringJdbc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaintingRepository {

  static PaintingRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new PaintingRepositoryJdbc();
      case "spring-jdbc" -> new PaintingRepositorySpringJdbc();
      default -> new PaintingRepositoryHibernate();
    };
  }

  PaintingEntity create(PaintingEntity painting);

  PaintingEntity update(PaintingEntity painting);

  Optional<PaintingEntity> findById(UUID id);

  Optional<PaintingEntity> findByTitle(String title);

  Optional<PaintingEntity> findByDescription(String description);

  List<PaintingEntity> findAllPainting();

  void remove(PaintingEntity painting);
}

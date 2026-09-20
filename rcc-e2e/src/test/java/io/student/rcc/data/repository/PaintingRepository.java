package io.student.rcc.data.repository;

import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.repository.impl.PaintingRepositoryHibernate;
import io.student.rcc.data.repository.impl.PaintingRepositoryJdbc;
import io.student.rcc.data.repository.impl.PaintingRepositorySpringJdbc;

import javax.annotation.Nonnull;
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

  @Nonnull PaintingEntity create(@Nonnull PaintingEntity painting);

  @Nonnull PaintingEntity update(@Nonnull PaintingEntity painting);

  @Nonnull Optional<PaintingEntity> findById(@Nonnull UUID id);

  @Nonnull Optional<PaintingEntity> findByTitle(@Nonnull String title);

  @Nonnull Optional<PaintingEntity> findByDescription(@Nonnull String description);

  @Nonnull List<PaintingEntity> findAllPainting();

  void remove(@Nonnull PaintingEntity painting);
}

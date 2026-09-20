package io.student.rcc.data.repository;

import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.repository.impl.CountryRepositoryHibernate;
import io.student.rcc.data.repository.impl.CountryRepositoryJdbc;
import io.student.rcc.data.repository.impl.CountryRepositorySpringJdbc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository {
  static CountryRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new CountryRepositoryJdbc();
      case "spring-jdbc" -> new CountryRepositorySpringJdbc();
      default -> new CountryRepositoryHibernate();
    };
  }

  @Nonnull Optional<CountryEntity> findById(@Nonnull UUID id);

  @Nonnull Optional<CountryEntity> findByName(@Nonnull String name);

  @Nonnull List<CountryEntity> findAll();
}

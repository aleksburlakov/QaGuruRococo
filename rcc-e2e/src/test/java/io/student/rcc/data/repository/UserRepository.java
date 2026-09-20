package io.student.rcc.data.repository;

import io.student.rcc.data.entity.UserEntity;
import io.student.rcc.data.repository.impl.UserRepositoryHibernate;
import io.student.rcc.data.repository.impl.UserRepositoryJdbc;
import io.student.rcc.data.repository.impl.UserRepositorySpringJdbc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  static UserRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new UserRepositoryJdbc();
      case "spring-jdbc" -> new UserRepositorySpringJdbc();
      default -> new UserRepositoryHibernate();
    };
  }

  @Nonnull UserEntity create(@Nonnull UserEntity user);

  @Nonnull UserEntity update(@Nonnull UserEntity user);

  @Nonnull Optional<UserEntity> findById(@Nonnull UUID id);

  @Nonnull Optional<UserEntity> findByUsername(@Nonnull String username);

  @Nonnull List<UserEntity> findAll();

  void remove(@Nonnull UserEntity user);
}

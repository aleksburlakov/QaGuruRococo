package io.student.rcc.data.repository;

import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.repository.impl.AuthUserRepositoryHibernate;
import io.student.rcc.data.repository.impl.AuthUserRepositoryJdbc;
import io.student.rcc.data.repository.impl.AuthUserRepositorySpringJdbc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
  static AuthUserRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new AuthUserRepositoryJdbc();
      case "spring-jdbc" -> new AuthUserRepositorySpringJdbc();
      default -> new AuthUserRepositoryHibernate();
    };
  }

  @Nonnull AuthUserEntity create(@Nonnull AuthUserEntity user);

  @Nonnull Optional<AuthUserEntity> findById(@Nonnull UUID id);

  @Nonnull List<AuthUserEntity> findAll();

  @Nonnull Optional<AuthUserEntity> findByUsername(@Nonnull String username);

  void remove(@Nonnull AuthUserEntity user);
}

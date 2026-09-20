package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class AuthUserRepositoryHibernate implements AuthUserRepository {
  private static final Config CFG = Config.getInstance();

  private final EntityManager entityManager = em(CFG.authJdbcUrl());

  @Override
  public @NonNull AuthUserEntity create(@NonNull AuthUserEntity user) {
    entityManager.joinTransaction();
    entityManager.persist(user);
    return user;
  }

  @Override
  public @NonNull Optional<AuthUserEntity> findById(@NonNull UUID id) {
    return Optional.ofNullable(
        entityManager.find(AuthUserEntity.class, id)
    );
  }

  @Override
  public @NonNull List<AuthUserEntity> findAll() {
    return entityManager.createQuery("select u from AuthUserEntity", AuthUserEntity.class)
        .getResultList();
  }

  @Override
  public @NonNull Optional<AuthUserEntity> findByUsername(@NonNull String username) {
    try {
      return Optional.of(
          entityManager.createQuery("select u from AuthUserEntity u where u.username =: username", AuthUserEntity.class)
              .setParameter("username", username)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public void remove(@NonNull AuthUserEntity user) {
    entityManager.joinTransaction();
    AuthUserEntity deletedAuthUser = entityManager.find(AuthUserEntity.class, user.getId());
    if (deletedAuthUser != null) {
      entityManager.remove(deletedAuthUser);
    }
  }
}

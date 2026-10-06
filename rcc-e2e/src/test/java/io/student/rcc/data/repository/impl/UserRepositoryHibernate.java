package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.UserEntity;
import io.student.rcc.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class UserRepositoryHibernate implements UserRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.apiJdbcUrl());

  @Override
  public @NonNull UserEntity create(@NonNull UserEntity user) {
    entityManager.joinTransaction();
    entityManager.persist(user);
    return user;
  }

  @Nonnull
  @Override
  public Optional<UserEntity> findById(@NonNull UUID id) {
    return Optional.ofNullable(
        entityManager.find(UserEntity.class, id)
    );
  }

  @Override
  public @NonNull Optional<UserEntity> findByUsername(@NonNull String username) {
    try {
      return Optional.of(
          entityManager.createQuery("select u from UserEntity u where u.username =: username", UserEntity.class)
              .setParameter("username", username)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Nonnull
  @Override
  public List<UserEntity> findAll() {
    return entityManager.createQuery("select u from UserEntity u", UserEntity.class)
            .getResultList();
  }

  @Override
  public void remove(@NonNull UserEntity user) {
    entityManager.joinTransaction();
    UserEntity deletedUser = entityManager.find(UserEntity.class, user.getId());
    if (deletedUser != null) {
      entityManager.remove(deletedUser);
    }
  }

  @Override
  public @NonNull UserEntity update(@NonNull UserEntity user) {
    return entityManager.merge(user);
  }
}

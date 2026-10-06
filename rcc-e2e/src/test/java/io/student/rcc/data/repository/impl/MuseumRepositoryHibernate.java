package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.repository.MuseumRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class MuseumRepositoryHibernate implements MuseumRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.apiJdbcUrl());

  @Override
  public @NonNull MuseumEntity create(@NonNull MuseumEntity museum) {
    entityManager.joinTransaction();
    entityManager.persist(museum);
    return museum;
  }

  @Override
  public @NonNull MuseumEntity update(@NonNull MuseumEntity museum) {
    entityManager.joinTransaction();
    entityManager.merge(museum);
    return museum;
  }

  @Override
  public @NonNull Optional<MuseumEntity> findById(@NonNull UUID id) {
    return Optional.ofNullable(entityManager.find(MuseumEntity.class, id));
  }

  @Override
  public @NonNull Optional<MuseumEntity> findByTitle(@NonNull String title) {
    try {
      return Optional.of(
          entityManager.createQuery("select m from MuseumEntity m where m.title = :title", MuseumEntity.class)
              .setParameter("title", title)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public @NonNull List<MuseumEntity> findAll() {
    return entityManager.createQuery("select m from MuseumEntity m", MuseumEntity.class)
        .getResultList();
  }

  @Override
  public void remove(@NonNull MuseumEntity museumEntity) {
    entityManager.joinTransaction();
    MuseumEntity deleteMuseum = entityManager.find(MuseumEntity.class, museumEntity.getId());
    if (deleteMuseum != null) {
      entityManager.remove(deleteMuseum);
    }
  }

  @Override
  public void removeAll() {
    entityManager.joinTransaction();
    entityManager.createQuery("DELETE FROM MuseumEntity").executeUpdate();
  }
}

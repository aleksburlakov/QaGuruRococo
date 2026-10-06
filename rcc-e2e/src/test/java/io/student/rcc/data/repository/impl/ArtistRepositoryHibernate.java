package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.repository.ArtistRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class ArtistRepositoryHibernate implements ArtistRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.apiJdbcUrl());

  @Override
  public @NonNull ArtistEntity create(@NonNull ArtistEntity artist) {
    entityManager.joinTransaction();
    entityManager.persist(artist);
    return artist;
  }

  @Override
  public @NonNull ArtistEntity update(@NonNull ArtistEntity artist) {
    return entityManager.merge(artist);
  }

  @Override
  public @NonNull Optional<ArtistEntity> findById(@NonNull UUID id) {
    return Optional.ofNullable(
        entityManager.find(ArtistEntity.class, id)
    );
  }

  @Override
  public @NonNull Optional<ArtistEntity> findByName(@NonNull String name) {
    try {
      return Optional.of(
          entityManager.createQuery("select a from ArtistEntity a where a.name =: name", ArtistEntity.class)
              .setParameter("name", name)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public @NonNull List<ArtistEntity> findAll() {
    return entityManager.createQuery("select a from ArtistEntity a", ArtistEntity.class)
        .getResultList();
  }

  @Override
  public void remove(@NonNull ArtistEntity artistEntity) {
    entityManager.joinTransaction();
    ArtistEntity deleteArtist = entityManager.find(ArtistEntity.class, artistEntity.getId());
    if (deleteArtist != null) {
      entityManager.remove(deleteArtist);
    }
  }

  @Override
  public void removeAll() {
    entityManager.joinTransaction();
    entityManager.createQuery("DELETE FROM ArtistEntity").executeUpdate();
  }
}

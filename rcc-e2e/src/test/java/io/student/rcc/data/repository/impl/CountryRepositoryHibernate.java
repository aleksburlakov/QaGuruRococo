package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.repository.CountryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class CountryRepositoryHibernate implements CountryRepository {
  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.apiJdbcUrl());

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    return Optional.ofNullable(
        entityManager.find(CountryEntity.class, id)
    );
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    try {
      return Optional.of(
          entityManager.createQuery("select c from CountryEntity c where c.name =: name", CountryEntity.class)
              .setParameter("name", name)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<CountryEntity> findAll() {
    return entityManager.createQuery("select c from CountryEntity c", CountryEntity.class)
        .getResultList();
  }
}

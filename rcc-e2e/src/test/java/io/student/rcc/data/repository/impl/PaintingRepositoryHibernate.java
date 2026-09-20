package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.repository.PaintingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class PaintingRepositoryHibernate implements PaintingRepository {
    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.apiJdbcUrl());

    @Override
    public @NonNull PaintingEntity create(@NonNull PaintingEntity painting) {
        entityManager.joinTransaction();
        entityManager.persist(painting);
        return painting;
    }

    @Override
    public @NonNull PaintingEntity update(@NonNull PaintingEntity painting) {
        entityManager.joinTransaction();
        entityManager.merge(painting);
        return painting;
    }

    @Override
    public @NonNull Optional<PaintingEntity> findById(@NonNull UUID id) {
        return Optional.ofNullable(entityManager.find(PaintingEntity.class, id));
    }

    @Override
    public @NonNull Optional<PaintingEntity> findByTitle(@NonNull String title) {
        try {
            return Optional.of(
                entityManager.createQuery("select p from PaintingEntity p where p.title = :title", PaintingEntity.class)
                    .setParameter("title", title)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Optional<PaintingEntity> findByDescription(@NonNull String description) {
        try {
            return Optional.of(
                entityManager.createQuery("select p from PaintingEntity p where p.description = :description", PaintingEntity.class)
                    .setParameter("description", description)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @NonNull List<PaintingEntity> findAllPainting() {
        return entityManager.createQuery("select p from PaintingEntity p", PaintingEntity.class)
            .getResultList();
    }

    @Override
    public void remove(@NonNull PaintingEntity painting) {
        entityManager.joinTransaction();
        PaintingEntity deletePainting = entityManager.find(PaintingEntity.class, painting.getId());
        if (deletePainting != null) {
            entityManager.remove(deletePainting);
        }
    }
}

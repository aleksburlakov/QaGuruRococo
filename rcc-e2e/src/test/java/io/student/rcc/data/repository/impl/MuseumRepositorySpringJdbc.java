package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.extractor.MuseumExtractor;
import io.student.rcc.data.repository.MuseumRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class MuseumRepositorySpringJdbc implements MuseumRepository {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

  @Override
  public MuseumEntity create(MuseumEntity museum) {
    UUID id = museum.getId() != null ? museum.getId() : UUID.randomUUID();
    jdbcTemplate.update(
        "INSERT INTO museum (id, title, description, city, photo, country_id) " +
            "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, UUID_TO_BIN(?))",
        id.toString(),
        museum.getTitle(),
        museum.getDescription(),
        museum.getCity(),
        museum.getPhoto(),
        museum.getCountry().getId() != null ?
            museum.getCountry().getId().toString() : null
    );

    museum.setId(id);
    return museum;
  }

  @Override
  public MuseumEntity update(MuseumEntity museum) {
    int updatedRows = jdbcTemplate.update(
        "UPDATE museum SET title = ?, description = ?, city = ?, photo = ?, country_id = UUID_TO_BIN(?) " +
            "WHERE id = UUID_TO_BIN(?)",
        museum.getTitle(),
        museum.getDescription(),
        museum.getCity(),
        museum.getPhoto(),
        museum.getCountry().getId() != null ?
            museum.getCountry().getId().toString() : null,
        museum.getId().toString()
    );

    if (updatedRows == 0) {
      throw new RuntimeException("Museum with id = " + museum.getId() + " was not found");
    }
    return museum;
  }

  @Override
  public Optional<MuseumEntity> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT " +
                  "BIN_TO_UUID(m.id) as museum_id, " +
                  "m.title, " +
                  "m.description, " +
                  "m.city, " +
                  "m.photo, " +
                  "BIN_TO_UUID(m.country_id) as country_id, " +
                  "c.name " +
                  "FROM museum m " +
                  "JOIN country c ON m.country_id = c.id " +
                  "WHERE m.id = UUID_TO_BIN(?)",
              MuseumExtractor.instance,
              id.toString()
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<MuseumEntity> findByTitle(String title) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT " +
                  "BIN_TO_UUID(m.id) as museum_id, " +
                  "m.title, " +
                  "m.description, " +
                  "m.city, " +
                  "m.photo, " +
                  "BIN_TO_UUID(m.country_id) as country_id, " +
                  "c.name " +
                  "FROM museum m " +
                  "JOIN country c ON m.country_id = c.id " +
                  "WHERE m.title = ?",
              MuseumExtractor.instance,
              title
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<MuseumEntity> findAll() {
    return jdbcTemplate.query(
        "SELECT " +
            "BIN_TO_UUID(m.id) as museum_id, " +
            "m.title, " +
            "m.description, " +
            "m.city, " +
            "m.photo, " +
            "BIN_TO_UUID(m.country_id) as country_id, " +
            "c.name " +
            "FROM museum m " +
            "JOIN country c ON m.country_id = c.id",
        MuseumExtractor.instance
    );
  }

  @Override
  public void remove(MuseumEntity museumEntity) {
    jdbcTemplate.update("DELETE FROM museum WHERE id = UUID_TO_BIN(?)", museumEntity.getId().toString());
  }

  @Override
  public void removeAll() {
    jdbcTemplate.update("DELETE FROM museum");
  }
}

package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.extractor.PaintingExtractor;
import io.student.rcc.data.repository.PaintingRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class PaintingRepositorySpringJdbc implements PaintingRepository {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

  @Override
  public PaintingEntity create(PaintingEntity painting) {
    UUID id = painting.getId() != null ? painting.getId() : UUID.randomUUID();
    jdbcTemplate.update(
        "INSERT INTO painting (id, title, description, artist_id, museum_id, content) " +
            "VALUES (UUID_TO_BIN(?), ?, ?, UUID_TO_BIN(?), UUID_TO_BIN(?), ?)",
        id.toString(),
        painting.getTitle(),
        painting.getDescription(),
        painting.getArtist() != null ? painting.getArtist().getId().toString() : null,
        painting.getMuseum() != null ? painting.getMuseum().getId().toString() : null,
        painting.getContent()
    );

    painting.setId(id);
    return painting;
  }

  @Override
  public PaintingEntity update(PaintingEntity painting) {
    int updatedRows = jdbcTemplate.update(
        "UPDATE painting SET title = ?, description = ?, artist_id = UUID_TO_BIN(?), museum_id = UUID_TO_BIN(?), content = ? " +
            "WHERE id = UUID_TO_BIN(?)",
        painting.getTitle(),
        painting.getDescription(),
        painting.getArtist() != null ? painting.getArtist().getId().toString() : null,
        painting.getMuseum() != null ? painting.getMuseum().getId().toString() : null,
        painting.getContent(),
        painting.getId().toString()
    );

    if (updatedRows == 0) {
      throw new RuntimeException("Painting with id = " + painting.getId() + " was not found");
    }
    return painting;
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT " +
                  "BIN_TO_UUID(p.id) as painting_id, " +
                  "p.title, " +
                  "p.description, " +
                  "p.content, " +
                  "BIN_TO_UUID(p.artist_id) as artist_id, " +
                  "a.name as artist_name, " +
                  "a.biography as artist_biography, " +
                  "a.photo as artist_photo, " +
                  "BIN_TO_UUID(p.museum_id) as museum_id, " +
                  "m.title as museum_title, " +
                  "m.description as museum_description, " +
                  "m.city as museum_city, " +
                  "m.photo as museum_photo, " +
                  "BIN_TO_UUID(m.country_id) as country_id," +
                  "c.name as country_name " +
                  "FROM painting p " +
                  "JOIN artist a ON p.artist_id = a.id " +
                  "JOIN museum m ON p.museum_id = m.id " +
                  "LEFT JOIN country c ON m.country_id = c.id " +
                  "WHERE p.id = UUID_TO_BIN(?)",
              PaintingExtractor.instance,
              id.toString()
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT " +
                  "BIN_TO_UUID(p.id) as painting_id, " +
                  "p.title, " +
                  "p.description, " +
                  "p.content, " +
                  "BIN_TO_UUID(p.artist_id) as artist_id, " +
                  "a.name as artist_name, " +
                  "a.biography as artist_biography, " +
                  "a.photo as artist_photo, " +
                  "BIN_TO_UUID(p.museum_id) as museum_id, " +
                  "m.title as museum_title, " +
                  "m.description as museum_description, " +
                  "m.city as museum_city, " +
                  "m.photo as museum_photo, " +
                  "BIN_TO_UUID(m.country_id) as country_id," +
                  "c.name as country_name " +
                  "FROM painting p " +
                  "JOIN artist a ON p.artist_id = a.id " +
                  "JOIN museum m ON p.museum_id = m.id " +
                  "LEFT JOIN country c ON m.country_id = c.id " +
                  "WHERE p.title = ?",
              PaintingExtractor.instance,
              title
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<PaintingEntity> findByDescription(String description) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT " +
                  "BIN_TO_UUID(p.id) as painting_id, " +
                  "p.title, " +
                  "p.description, " +
                  "p.content, " +
                  "BIN_TO_UUID(p.artist_id) as artist_id, " +
                  "a.name as artist_name, " +
                  "a.biography as artist_biography, " +
                  "a.photo as artist_photo, " +
                  "BIN_TO_UUID(p.museum_id) as museum_id, " +
                  "m.title as museum_title, " +
                  "m.description as museum_description, " +
                  "m.city as museum_city, " +
                  "m.photo as museum_photo, " +
                  "BIN_TO_UUID(m.country_id) as country_id," +
                  "c.name as country_name " +
                  "FROM painting p " +
                  "JOIN artist a ON p.artist_id = a.id " +
                  "JOIN museum m ON p.museum_id = m.id " +
                  "LEFT JOIN country c ON m.country_id = c.id " +
                  "WHERE p.id = ?",
              PaintingExtractor.instance,
              description
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<PaintingEntity> findAllPainting() {
    return jdbcTemplate.query(
        "SELECT " +
            "BIN_TO_UUID(p.id) as painting_id, " +
            "p.title, " +
            "p.description, " +
            "p.content, " +
            "BIN_TO_UUID(p.artist_id) as artist_id, " +
            "a.name as artist_name, " +
            "a.biography as artist_biography, " +
            "a.photo as artist_photo, " +
            "BIN_TO_UUID(p.museum_id) as museum_id, " +
            "m.title as museum_title, " +
            "m.description as museum_description, " +
            "m.city as museum_city, " +
            "m.photo as museum_photo, " +
            "BIN_TO_UUID(m.country_id) as country_id," +
            "c.name as country_name " +
            "FROM painting p " +
            "JOIN artist a ON p.artist_id = a.id " +
            "JOIN museum m ON p.museum_id = m.id " +
            "LEFT JOIN country c ON m.country_id = c.id",
        PaintingExtractor.instance
    );
  }

  @Override
  public void remove(PaintingEntity painting) {
    jdbcTemplate.update("DELETE FROM painting WHERE id = UUID_TO_BIN(?)", painting.getId().toString());
  }
}

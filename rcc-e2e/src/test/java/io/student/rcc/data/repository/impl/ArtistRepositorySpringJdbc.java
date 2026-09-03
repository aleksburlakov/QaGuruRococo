package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.mapper.ArtistEntityRowMapper;
import io.student.rcc.data.repository.ArtistRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ArtistRepositorySpringJdbc implements ArtistRepository {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

  @Override
  public ArtistEntity create(ArtistEntity artist) {
    UUID id = artist.getId() != null ? artist.getId() : UUID.randomUUID();
    jdbcTemplate.update(
        "INSERT INTO artist (id, name, biography, photo) " +
            "VALUES (UUID_TO_BIN(?), ?, ?, ?)",
        id.toString(),
        artist.getName(),
        artist.getBiography(),
        artist.getPhoto()
    );

    artist.setId(id);
    return artist;
  }

  @Override
  public ArtistEntity update(ArtistEntity artist) {
    jdbcTemplate.update(
        "UPDATE artist SET name = ?, biography = ?, photo = ? " +
            "WHERE id = UUID_TO_BIN(?)",
        artist.getName(),
        artist.getBiography(),
        artist.getPhoto(),
        artist.getId().toString()
    );
    return artist;
  }

  @Override
  public Optional<ArtistEntity> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              "SELECT BIN_TO_UUID(id) as id, name, biography, photo " +
                  "FROM artist WHERE id = UUID_TO_BIN(?)",
              ArtistEntityRowMapper.instance,
              id.toString()
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<ArtistEntity> findByName(String name) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              "SELECT BIN_TO_UUID(id) as id, name, biography, photo " +
                  "FROM artist WHERE name = ?",
              ArtistEntityRowMapper.instance,
              name
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<ArtistEntity> findAll() {
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id) as id, name, biography, photo FROM artist",
        ArtistEntityRowMapper.instance
    );
  }

  @Override
  public void remove(ArtistEntity artistEntity) {
    jdbcTemplate.update("DELETE FROM artist WHERE id = UUID_TO_BIN(?)", artistEntity.getId().toString());
  }

  @Override
  public void removeAll() {
    jdbcTemplate.execute("DELETE FROM artist");
  }
}

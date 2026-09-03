package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.mapper.CountryEntityRowMapper;
import io.student.rcc.data.repository.CountryRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CountryRepositorySpringJdbc implements CountryRepository {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              "SELECT BIN_TO_UUID(id) as id, name FROM country WHERE id = UUID_TO_BIN(?)",
              CountryEntityRowMapper.instance,
              id.toString()
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              "SELECT BIN_TO_UUID(id) as id, name FROM country WHERE name = ?",
              CountryEntityRowMapper.instance,
              name
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<CountryEntity> findAll() {
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id) as id, name FROM country",
        CountryEntityRowMapper.instance
    );
  }
}

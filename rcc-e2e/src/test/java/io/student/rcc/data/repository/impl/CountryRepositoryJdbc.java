package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.repository.CountryRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class CountryRepositoryJdbc implements CountryRepository {
  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.apiJdbcUrl();

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as country_id, name FROM country WHERE id = UUID_TO_BIN(?)")) {
      ps.setString(1, id.toString());
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.of(mapRow(rs));
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as country_id, name FROM country WHERE name = ?")) {
      ps.setString(1, name);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.of(mapRow(rs));
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CountryEntity> findAll() {
    List<CountryEntity> resultList = new ArrayList<>();
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as country_id, name FROM country")) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          resultList.add(mapRow(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return resultList;
  }

  private CountryEntity mapRow(ResultSet rs) throws SQLException {
    CountryEntity country = new CountryEntity();
    country.setId(UUID.fromString(rs.getString("country_id")));
    country.setName(rs.getString("name"));
    return country;
  }
}

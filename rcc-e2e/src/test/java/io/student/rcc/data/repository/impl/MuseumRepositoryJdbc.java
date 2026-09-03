package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.repository.MuseumRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class MuseumRepositoryJdbc implements MuseumRepository {

  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.apiJdbcUrl();

  @Override
  public MuseumEntity create(MuseumEntity museum) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
        "INSERT INTO museum (id, title, description, city, photo, country_id) " +
            "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, UUID_TO_BIN(?))")) {
      UUID id = museum.getId() != null ? museum.getId() : UUID.randomUUID();
      ps.setString(1, id.toString());
      ps.setString(2, museum.getTitle());
      ps.setString(3, museum.getDescription());
      ps.setString(4, museum.getCity());
      ps.setBytes(5, museum.getPhoto());
      ps.setString(6,
          museum.getCountry().getId() != null ?
              museum.getCountry().getId().toString() : null);
      ps.executeUpdate();

      museum.setId(id);
      return museum;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public MuseumEntity update(MuseumEntity museum) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
        "UPDATE museum SET title = ?, description = ?, city = ?, photo = ?, country_id = UUID_TO_BIN(?) " +
            "WHERE id = UUID_TO_BIN(?)"
    )) {
      ps.setString(1, museum.getTitle());
      ps.setString(2, museum.getDescription());
      ps.setString(3, museum.getCity());
      ps.setBytes(4, museum.getPhoto());
      ps.setString(5, museum.getCountry().getId() != null ?
          museum.getCountry().getId().toString() : null);
      ps.setString(6, museum.getId().toString());

      int updatedRows = ps.executeUpdate();
      if (updatedRows == 0) {
        throw new SQLException("Museum with id = " + museum.getId() + " was not found");
      }
      return museum;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<MuseumEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
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
            "WHERE m.id = UUID_TO_BIN(?)"
    )) {
      ps.setObject(1, id.toString());
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
  public Optional<MuseumEntity> findByTitle(String title) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
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
            "WHERE m.title = ?"
    )) {
      ps.setObject(1, title);
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
  public List<MuseumEntity> findAll() {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
        "SELECT " +
            "BIN_TO_UUID(m.id) as museum_id, " +
            "m.title, " +
            "m.description, " +
            "m.city, " +
            "m.photo, " +
            "BIN_TO_UUID(m.country_id) as country_id, " +
            "c.name " +
            "FROM museum m " +
            "JOIN country c ON m.country_id = c.id"
    )) {
      ps.execute();
      List<MuseumEntity> result = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          result.add(mapRow(rs));
        }
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(MuseumEntity museumEntity) {
    try (PreparedStatement deletePs = holder(URL).connection().prepareStatement(
        "DELETE FROM museum WHERE id = UUID_TO_BIN(?)"
    )) {
      deletePs.setObject(1, museumEntity.getId().toString());
      deletePs.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeAll() {
    try (PreparedStatement deletePs = holder(URL).connection().prepareStatement(
        "DELETE FROM museum"
    )) {
      deletePs.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private MuseumEntity mapRow(ResultSet rs) throws SQLException {
    MuseumEntity museum = new MuseumEntity();
    museum.setId(UUID.fromString(rs.getString("museum_id")));
    museum.setTitle(rs.getString("title"));
    museum.setDescription(rs.getString("description"));
    museum.setCity(rs.getString("city"));
    museum.setPhoto(rs.getBytes("photo"));

    String countryId = rs.getString("country_id");
    if (countryId != null) {
      CountryEntity country = new CountryEntity();
      country.setId(UUID.fromString(countryId));
      country.setName(rs.getString("name"));
      museum.setCountry(country);
    }

    return museum;
  }
}

package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.repository.ArtistRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class ArtistRepositoryJdbc implements ArtistRepository {

  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.apiJdbcUrl();

  @Override
  public ArtistEntity create(ArtistEntity artist) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
        "INSERT INTO artist (id, name, biography, photo) " +
            "VALUES (UUID_TO_BIN(?), ?, ?, ?)")) {
      UUID id = artist.getId() != null ? artist.getId() : UUID.randomUUID();
      ps.setString(1, id.toString());
      ps.setString(2, artist.getName());
      ps.setString(3, artist.getBiography());
      ps.setBytes(4, artist.getPhoto());
      ps.executeUpdate();

      artist.setId(id);
      return artist;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ArtistEntity update(ArtistEntity artist) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
        "UPDATE artist SET name = ?, biography = ?, photo = ? " +
            "WHERE id = UUID_TO_BIN(?)")) {

      ps.setString(1, artist.getName());
      ps.setString(2, artist.getBiography());
      ps.setBytes(3, artist.getPhoto());
      ps.setString(4, artist.getId().toString());

      int updatedRows = ps.executeUpdate();
      if (updatedRows == 0) {
        throw new SQLException("Artist with id = " + artist.getId() + " was not found");
      }
      return artist;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<ArtistEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as id, name, biography, photo " +
            "FROM artist WHERE id = UUID_TO_BIN(?)")) {
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
  public Optional<ArtistEntity> findByName(String name) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as id, name, biography, photo " +
            "FROM artist WHERE name = ?")) {
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
  public List<ArtistEntity> findAll() {
    List<ArtistEntity> resultList = new ArrayList<>();
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as id, name, biography, photo FROM artist")) {
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

  @Override
  public void remove(ArtistEntity artistEntity) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("DELETE FROM artist WHERE id = UUID_TO_BIN(?)")) {
      ps.setString(1, artistEntity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeAll() {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("DELETE FROM artist")) {
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private ArtistEntity mapRow(ResultSet rs) throws SQLException {
    ArtistEntity artist = new ArtistEntity();
    artist.setId(UUID.fromString(rs.getString("id")));
    artist.setName(rs.getString("name"));
    artist.setBiography(rs.getString("biography"));
    artist.setPhoto(rs.getBytes("photo"));
    return artist;
  }
}

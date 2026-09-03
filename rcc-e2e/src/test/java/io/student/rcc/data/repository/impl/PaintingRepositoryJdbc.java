package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.repository.PaintingRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class PaintingRepositoryJdbc implements PaintingRepository {

  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.apiJdbcUrl();

  @Override
  public PaintingEntity create(PaintingEntity painting) {
    try (PreparedStatement paintingPs = holder(URL).connection().prepareStatement(
        "INSERT INTO painting (id, title, description, artist_id, museum_id, content) " +
            "VALUES (UUID_TO_BIN(?), ?, ?, UUID_TO_BIN(?), UUID_TO_BIN(?), ?)")) {
      UUID id = painting.getId() != null ? painting.getId() : UUID.randomUUID();
      paintingPs.setString(1, id.toString());
      paintingPs.setString(2, painting.getTitle());
      paintingPs.setString(3, painting.getDescription());
      paintingPs.setString(4,
          painting.getArtist() != null ?
              painting.getArtist().getId().toString() : null);
      paintingPs.setString(5,
          painting.getMuseum() != null ?
              painting.getMuseum().getId().toString() : null);
      paintingPs.setBytes(6, painting.getContent());
      paintingPs.executeUpdate();

      painting.setId(id);
      return painting;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public PaintingEntity update(PaintingEntity painting) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
        "UPDATE painting SET title = ?, description = ?, artist_id = UUID_TO_BIN(?), museum_id = UUID_TO_BIN(?), content = ? " +
            "WHERE id = UUID_TO_BIN(?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, painting.getTitle());
      ps.setString(2, painting.getDescription());
      ps.setString(3, painting.getArtist() != null ?
          painting.getArtist().getId().toString() : null);
      ps.setString(4, painting.getMuseum() != null ?
          painting.getMuseum().getId().toString() : null);
      ps.setBytes(5, painting.getContent());
      ps.setString(6, painting.getId().toString());

      int updatedRows = ps.executeUpdate();
      if (updatedRows == 0) {
        throw new SQLException("Painting with id = " + painting.getId() + " was not found");
      }
      return painting;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
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
            "JOIN country c ON m.country_id = c.id " +
            "WHERE p.id = UUID_TO_BIN(?)"
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
  public Optional<PaintingEntity> findByTitle(String title) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
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
            "JOIN country c ON m.country_id = c.id " +
            "WHERE p.title = ?"
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
  public Optional<PaintingEntity> findByDescription(String description) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
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
            "JOIN country c ON m.country_id = c.id " +
            "WHERE p.description = ?"
    )) {
      ps.setObject(1, description);
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
  public List<PaintingEntity> findAllPainting() {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
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
            "JOIN country c ON m.country_id = c.id")) {
      ps.execute();
      List<PaintingEntity> result = new ArrayList<>();
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
  public void remove(PaintingEntity painting) {
    try (PreparedStatement deletePs = holder(URL).connection().prepareStatement(
        "DELETE FROM painting WHERE id = UUID_TO_BIN(?)"
    )) {
      deletePs.setObject(1, painting.getId().toString());
      deletePs.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private PaintingEntity mapRow(ResultSet rs) throws SQLException {
    PaintingEntity painting = new PaintingEntity();
    painting.setId(rs.getObject("painting_id", UUID.class));
    painting.setTitle(rs.getString("title"));
    painting.setDescription(rs.getString("description"));
    painting.setContent(rs.getBytes("content"));

    String artistId = rs.getString("artist_id");
    if (artistId != null) {
      ArtistEntity artist = new ArtistEntity();
      artist.setId(UUID.fromString(artistId));
      artist.setName(rs.getString("artist_name"));
      artist.setBiography(rs.getString("artist_biography"));
      artist.setPhoto(rs.getBytes("artist_photo"));
      painting.setArtist(artist);
    }

    String museumId = rs.getString("museum_id");
    if (museumId != null) {
      MuseumEntity museum = new MuseumEntity();
      museum.setId(UUID.fromString(museumId));
      museum.setTitle(rs.getString("museum_title"));
      museum.setDescription(rs.getString("museum_description"));
      museum.setCity(rs.getString("museum_city"));
      museum.setPhoto(rs.getBytes("museum_photo"));

      String countryId = rs.getString("country_id");
      if (countryId != null) {
        CountryEntity country = new CountryEntity();
        country.setId(UUID.fromString(countryId));
        country.setName(rs.getString("country_name"));
        museum.setCountry(country);
      }

      painting.setMuseum(museum);
    }

    return painting;
  }
}

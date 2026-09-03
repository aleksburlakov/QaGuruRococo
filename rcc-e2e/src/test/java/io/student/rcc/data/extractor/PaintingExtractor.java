package io.student.rcc.data.extractor;

import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.entity.PaintingEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PaintingExtractor implements ResultSetExtractor<List<PaintingEntity>> {

  public static final PaintingExtractor instance = new PaintingExtractor();

  private PaintingExtractor() {
  }

  @Override
  public List<PaintingEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
    Map<UUID, PaintingEntity> paintingMap = new LinkedHashMap<>();

    while (rs.next()) {
      UUID paintingId = UUID.fromString(rs.getString("painting_id"));
      PaintingEntity painting = paintingMap.get(paintingId);

      if (painting == null) {
        painting = new PaintingEntity();
        painting.setId(paintingId);
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

        paintingMap.put(paintingId, painting);
      }
    }

    return new ArrayList<>(paintingMap.values());
  }
}

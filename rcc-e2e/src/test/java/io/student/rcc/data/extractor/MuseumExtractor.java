package io.student.rcc.data.extractor;

import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MuseumExtractor implements ResultSetExtractor<List<MuseumEntity>> {

  public static final MuseumExtractor instance = new MuseumExtractor();

  private MuseumExtractor() {
  }

  @Override
  public List<MuseumEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
    Map<UUID, MuseumEntity> museumMap = new LinkedHashMap<>();

    while (rs.next()) {
      UUID museumId = UUID.fromString(rs.getString("museum_id"));
      MuseumEntity museum = museumMap.get(museumId);

      if (museum == null) {
        museum = new MuseumEntity();
        museum.setId(museumId);
        museum.setTitle(rs.getString("title"));
        museum.setDescription(rs.getString("description"));
        museum.setCity(rs.getString("city"));
        museum.setPhoto(rs.getBytes("photo"));

        String countryId = rs.getString("country_id");
        if (countryId != null && !rs.wasNull()) {
          CountryEntity country = new CountryEntity();
          country.setId(UUID.fromString(countryId));
          country.setName(rs.getString("name"));
          museum.setCountry(country);
        }

        museumMap.put(museumId, museum);
      }
    }

    return new ArrayList<>(museumMap.values());
  }
}

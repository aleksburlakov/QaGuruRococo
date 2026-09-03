package io.student.rcc.data.mapper;

import io.student.rcc.data.entity.CountryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CountryEntityRowMapper implements RowMapper<CountryEntity> {

  public static final CountryEntityRowMapper instance = new CountryEntityRowMapper();

  private CountryEntityRowMapper() {
  }

  @Override
  public CountryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CountryEntity countryEntity = new CountryEntity();
    countryEntity.setId(rs.getObject("id", UUID.class));
    countryEntity.setName(rs.getString("name"));
    return countryEntity;
  }
}

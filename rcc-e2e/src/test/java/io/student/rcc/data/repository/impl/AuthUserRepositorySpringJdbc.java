package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.entity.auth.Authority;
import io.student.rcc.data.entity.auth.AuthorityEntity;
import io.student.rcc.data.extractor.AuthUserExtractor;
import io.student.rcc.data.repository.AuthUserRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    UUID id = user.getId() != null ? user.getId() : UUID.randomUUID();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO user (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (UUID_TO_BIN(?),?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, id.toString());
      ps.setString(2, user.getUsername());
      ps.setString(3, user.getPassword());
      ps.setBoolean(4, user.getEnabled());
      ps.setBoolean(5, user.getAccountNonExpired());
      ps.setBoolean(6, user.getAccountNonLocked());
      ps.setBoolean(7, user.getCredentialsNonExpired());
      return ps;
    });

    user.setId(id);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
        authority -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setAuthority(authority);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);
    jdbcTemplate.batchUpdate(
        "INSERT INTO authority (user_id, authority) VALUES (UUID_TO_BIN(?), ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, authorityEntities[i].getUser().getId().toString());
            ps.setString(2, authorityEntities[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authorityEntities.length;
          }
        }
    );
    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT BIN_TO_UUID(a.id) AS authority_id, " +
                  "a.authority, " +
                  "BIN_TO_UUID(u.id) AS user_id, " +
                  "u.username, " +
                  "u.password, " +
                  "u.enabled, " +
                  "u.account_non_expired, " +
                  "u.account_non_locked, " +
                  "u.credentials_non_expired " +
                  "FROM user u JOIN authority a ON u.id = a.user_id " +
                  "WHERE u.id = UUID_TO_BIN(?)",
              AuthUserExtractor.instance,
              id
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<AuthUserEntity> findAll() {
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(a.id) AS authority_id, " +
            "a.authority, " +
            "BIN_TO_UUID(u.id) AS user_id, " +
            "u.username, " +
            "u.password, " +
            "u.enabled, " +
            "u.account_non_expired, " +
            "u.account_non_locked, " +
            "u.credentials_non_expired " +
            "FROM user u JOIN authority a ON u.id = a.user_id " +
            "WHERE u.id = ?",
        AuthUserExtractor.instance
    );
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.query(
              "SELECT BIN_TO_UUID(a.id) AS authority_id, " +
                  "authority, " +
                  "BIN_TO_UUID(user_id) AS id, " +
                  "u.username, " +
                  "u.password, " +
                  "u.enabled, " +
                  "u.account_non_expired, " +
                  "u.account_non_locked, " +
                  "u.credentials_non_expired " +
                  "FROM user u JOIN authority a ON u.id = a.user_id " +
                  "WHERE u.username = ?",
              AuthUserExtractor.instance,
              username
          ).getFirst()
      );
    } catch (EmptyResultDataAccessException | NoSuchElementException e) {
      return Optional.empty();
    }
  }

  @Override
  public void remove(AuthUserEntity user) {
    jdbcTemplate.update("DELETE FROM authority WHERE user_id = ?", user.getId());
    jdbcTemplate.update("DELETE FROM user WHERE id = ?", user.getId());
  }
}

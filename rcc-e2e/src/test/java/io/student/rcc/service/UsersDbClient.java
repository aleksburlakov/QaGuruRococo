package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.model.UserJson;
import java.sql.PreparedStatement;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsersDbClient implements UsersClient {

  private static Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(
      new SingleConnectionDataSource(
          CFG.authJdbcUrl(),
          CFG.dbUsername(),
          CFG.dbPassword(),
          false
      )
  );

  @Override
  public UserJson createUser(UserJson userJson) {
    final UUID userId = UUID.randomUUID();
    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              """
                        INSERT INTO `rococo-auth`.`user` (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                         VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)
                      """
          );
          ps.setString(1, userId.toString());
          ps.setString(2, userJson.username());
          ps.setString(3, passwordEncoder.encode(userJson.password()));
          ps.setBoolean(4, true);
          ps.setBoolean(5, true);
          ps.setBoolean(6, true);
          ps.setBoolean(7, true);
          return ps;
        }
    );
    return new UserJson(userId, userJson.username(), userJson.password(), userJson.firstname(), userJson.lastName(),
        userJson.avatar());
  }
}

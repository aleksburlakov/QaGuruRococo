package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.UserEntity;
import io.student.rcc.data.mapper.UserdataUserEntityRowMapper;
import io.student.rcc.data.repository.UserRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositorySpringJdbc implements UserRepository {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

  @Override
  public UserEntity create(UserEntity user) {
    UUID id = user.getId() != null ? user.getId() : UUID.randomUUID();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO user (id, username, firstname, lastname, avatar) " +
              "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?)"
      );
      ps.setString(1, id.toString());
      ps.setString(2, user.getUsername());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getLastname());
      ps.setBytes(5, user.getAvatar());
      return ps;
    });

    user.setId(id);
    return user;
  }

  @Override
  public UserEntity update(UserEntity user) {
    int updatedRows = jdbcTemplate.update(
        "UPDATE user SET username = ?, firstname = ?, lastname = ?, avatar = ? WHERE id = UUID_TO_BIN(?)",
        user.getUsername(),
        user.getFirstname(),
        user.getLastname(),
        user.getAvatar(),
        user.getId().toString()
    );

    if (updatedRows == 0) {
      throw new RuntimeException("User with id = " + user.getId() + " was not found");
    }
    return user;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              "SELECT BIN_TO_UUID(id) as id, username, firstname, lastname, avatar " +
                  "FROM user WHERE id = UUID_TO_BIN(?)",
              UserdataUserEntityRowMapper.instance,
              id.toString()
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id) as id, username, firstname, lastname, avatar " +
                "FROM user WHERE username = ?",
            UserdataUserEntityRowMapper.instance,
            username
        )
    );
  }

  @Override
  public List<UserEntity> findAll() {
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id) as id, username, firstname, lastname, avatar FROM user",
        UserdataUserEntityRowMapper.instance
    );
  }

  @Override
  public void remove(UserEntity user) {
    jdbcTemplate.update("DELETE FROM user WHERE id = ?", user.getId());
  }
}

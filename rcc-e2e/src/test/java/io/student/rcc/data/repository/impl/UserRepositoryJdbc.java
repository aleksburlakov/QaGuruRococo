package io.student.rcc.data.repository.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.UserEntity;
import io.student.rcc.data.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class UserRepositoryJdbc implements UserRepository {

  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.apiJdbcUrl();

  @Override
  public UserEntity create(UserEntity user) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
            "INSERT INTO user (id, username, firstname, lastname, avatar) VALUES (UUID_TO_BIN(?), ?, ?, ?, ?)")) {
      UUID id = user.getId() != null ? user.getId() : UUID.randomUUID();
      ps.setString(1, id.toString());
      ps.setString(2, user.getUsername());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getLastname());
      ps.setBytes(5, user.getAvatar());
      ps.executeUpdate();
      user.setId(id);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserEntity update(UserEntity user) {
    try (PreparedStatement ps = holder(URL).connection().prepareStatement(
            "UPDATE user SET username = ?, firstname = ?, lastname = ?, avatar = ? " +
                    "WHERE id = UUID_TO_BIN(?)")) {

      ps.setString(1, user.getUsername());
      ps.setString(2, user.getFirstname());
      ps.setString(3, user.getLastname());
      ps.setBytes(4, user.getAvatar());
      ps.setString(5, user.getId().toString());

      int updatedRows = ps.executeUpdate();
      if (updatedRows == 0) {
        throw new SQLException("User with id = " + user.getId() + " was not found");      }
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as id, username, firstname, lastname, avatar " +
            "FROM user WHERE id = UUID_TO_BIN(?)")) {
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
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as id, username, firstname, lastname, avatar " +
            "FROM user WHERE username = ?")) {
      ps.setObject(1, username);
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
  public List<UserEntity> findAll() {
    List<UserEntity> resultList = new ArrayList<UserEntity>();
    try (PreparedStatement ps = holder(URL).connection()
        .prepareStatement("SELECT BIN_TO_UUID(id) as id, username, firstname, lastname, avatar FROM user")) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          // проверить
          resultList.add(mapRow(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return resultList;
  }

  @Override
  public void remove(UserEntity user) {
    try (PreparedStatement deleteUserPs = holder(URL).connection().prepareStatement(
        "DELETE FROM user WHERE id = ?"
    )) {
      deleteUserPs.setObject(1, user.getId());
      deleteUserPs.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private UserEntity mapRow(ResultSet rs) throws SQLException {
    UserEntity result = new UserEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setFirstname(rs.getString("firstname"));
    result.setLastname(rs.getString("lastName"));
    result.setAvatar(rs.getBytes("avatar"));
    return result;
  }
}

package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.UserEntity;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.entity.auth.Authority;
import io.student.rcc.data.entity.auth.AuthorityEntity;
import io.student.rcc.data.repository.AuthUserRepository;
import io.student.rcc.data.repository.UserRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
  private final UserRepository userdataUserRepository = UserRepository.getInstance();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.apiJdbcUrl()
  );

  @Override
  public UserJson createUser(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity authUser = authUserEntity(username, password);
          authUserRepository.create(authUser);
          return UserJson.fromEntity(
              userdataUserRepository.create(userEntity(username))
          );
        }
    );
  }

  @Override
  public UserJson updateUser(UserJson userJson) {
    return xaTransactionTemplate.execute(() -> {
          UserEntity updatedUser = userdataUserRepository.update(UserEntity.fromJson(userJson));
          return UserJson.fromEntity(updatedUser);
        }
    );
  }

  @Override
  public Optional<UserJson> findUserById(UUID id) {
    return userdataUserRepository.findById(id)
        .map(UserJson::fromEntity);
  }

  @Override
  public Optional<UserJson> findUserByUsername(String username) {
    return userdataUserRepository.findByUsername(username)
        .map(UserJson::fromEntity);
  }

  @Override
  public List<UserJson> findAll() {
    return userdataUserRepository.findAll().stream().map(UserJson::fromEntity).collect(Collectors.toList());
  }

  @Override
  public void removeUser(UserJson userJson) {
    xaTransactionTemplate.execute(() -> {
      AuthUserEntity authUserEntity = authUserRepository.findByUsername(userJson.username()).get();
      authUserRepository.remove(authUserEntity);
      userdataUserRepository.remove(UserEntity.fromJson(userJson));
      return null;
    });
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(
        Arrays.stream(Authority.values()).map(
            e -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setUser(authUser);
              ae.setAuthority(e);
              return ae;
            }
        ).toList()
    );
    return authUser;
  }

  private UserEntity userEntity(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    return ue;
  }
}

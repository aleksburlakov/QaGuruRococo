package io.student.rcc.config;

import javax.annotation.Nonnull;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  @Nonnull
  String frontUrl();

  @Nonnull
  String authUrl();

  @Nonnull
  String authJdbcUrl();

  @Nonnull
  String apiUrl();

  @Nonnull
  String apiJdbcUrl();

  @Nonnull
  String dbUsername();

  @Nonnull
  String dbPassword();
}

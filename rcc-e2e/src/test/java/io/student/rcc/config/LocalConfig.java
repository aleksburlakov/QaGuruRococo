package io.student.rcc.config;

import org.jspecify.annotations.NonNull;

enum LocalConfig implements Config {
  INSTANCE;

  @Override
  public @NonNull String frontUrl() {
    return "http://localhost:3000";
  }

  @Override
  public @NonNull String authJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rococo-auth?serverTimezone=UTC&createDatabaseIfNotExist=true&pinGlobalTxToPhysicalConnection=true";
  }

  @Override
  public @NonNull String apiJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rococo-api?serverTimezone=UTC&createDatabaseIfNotExist=true&pinGlobalTxToPhysicalConnection=true";
  }

  @Override
  public @NonNull String dbUsername() {
    return "root";
  }

  @Override
  public @NonNull String dbPassword() {
    return "secret";
  }
}

package io.student.rcc.jupiter.extension;

import io.student.rcc.data.jpa.EntityManagers;
import io.student.rcc.data.tpl.Connections;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DatabasesExtension implements SuiteExtension {
  @Override
  public void afterSuite() {
    Connections.closeAllConnections();
    EntityManagers.closeAllEmfs();
  }
}

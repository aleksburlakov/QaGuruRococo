package io.student.rcc.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.extension.*;

public class BrowserExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Selenide.closeWebDriver();
    }
  }
}

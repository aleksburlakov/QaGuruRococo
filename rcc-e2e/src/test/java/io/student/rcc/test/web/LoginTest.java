package io.student.rcc.test.web;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @User
  @Test
  public void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson userJson) {
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(userJson.username(), userJson.password())
        .checkMainPageDisplayed();
  }

  @User
  @Test
  public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(UserJson userJson) {
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .unSuccessLogin(userJson.username(), userJson.username())
        .checkLoginErrorMessageText("Неверные учетные данные пользователя");
  }

  @AfterEach
  void cleanUp() {
    Selenide.clearBrowserCookies();
    Selenide.clearBrowserLocalStorage();
  }
}

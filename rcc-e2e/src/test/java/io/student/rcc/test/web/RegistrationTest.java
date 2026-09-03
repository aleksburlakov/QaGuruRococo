package io.student.rcc.test.web;

import static io.student.rcc.utils.TestDataGenerator.generateRandomLogin;
import static io.student.rcc.utils.TestDataGenerator.generateRandomPassword;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.TestDataExtension;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestDataExtension.class)
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();

  @Test
  public void shouldRegisterNewUser() {
    String userName = generateRandomLogin();
    String password = generateRandomPassword();
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .goToRegistrationPage()
        .registerNewUser(userName, password, password)
        .checkSuccessRegistrationTitle("Добро пожаловать в Rococo");
  }

  @Test
  public void shouldShowErrorIfPasswordAndConfirmPasswordIsError() {
    String userName = generateRandomLogin();
    String password = generateRandomPassword();
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .goToRegistrationPage()
        .registerNewUser(userName, password, password + "123")
        .checkErrorPasswordMessage("Passwords should be equal");
  }

  @User
  @Test
  public void shouldNotRegisterUserWithExistingUsername(UserJson user) {
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .goToRegistrationPage()
        .registerNewUser(user.username(), "12345", "12345")
        .checkUserAlreadyExistMessage(String.format("Username `%s` already exists", user.username()));
  }
}
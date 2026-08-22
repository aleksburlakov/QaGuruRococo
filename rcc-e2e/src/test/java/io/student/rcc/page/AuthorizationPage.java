package io.student.rcc.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class AuthorizationPage {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement enterButton = $("button[type='submit']");
  private final SelenideElement registerLink = $x("//a[text()='Зарегистрироваться']");
  private final SelenideElement errorLoginMessage = $("[class*='login__error']");

  public RegistrationPage goToRegistrationPage() {
    registerLink.click();
    return new RegistrationPage();
  }

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    enterButton.click();
    return new MainPage();
  }

  public AuthorizationPage unSuccessLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    enterButton.click();
    return this;
  }

  public AuthorizationPage checkLoginErrorMessageText(String loginErrorMessageText) {
    errorLoginMessage.shouldHave(text(loginErrorMessageText));
    return this;
  }
}

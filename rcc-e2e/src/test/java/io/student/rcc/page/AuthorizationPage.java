package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AuthorizationPage extends BasePage<AuthorizationPage> {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement enterButton = $("button[type='submit']");
  private final SelenideElement registerLink = $x("//a[text()='Зарегистрироваться']");
  private final SelenideElement errorLoginMessage = $("[class*='login__error']");

  @Step("Кликнуть по ссылке 'Зарегистрироваться'")
  public RegistrationPage goToRegistrationPage() {
    registerLink.click();
    return new RegistrationPage();
  }

  @Step("В поле 'Имя пользователя' ввести '{username}', в поле 'Пароль' значение '{password}' и нажать кнопку 'Войти'")
  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    enterButton.click();
    return new MainPage();
  }

  @Step("В поле 'Имя пользователя' ввести '{username}', в поле 'Пароль' значение '{password}' и нажать кнопку 'Войти'")
  public AuthorizationPage unSuccessLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    enterButton.click();
    return this;
  }

  @Step("Проверить текст в ошибки на соответствие значению '{loginErrorMessageText}'")
  public AuthorizationPage checkLoginErrorMessageText(String loginErrorMessageText) {
    errorLoginMessage.shouldHave(text(loginErrorMessageText));
    return this;
  }
}

package io.student.rcc.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class RegistrationPage extends BasePage<RegistrationPage> {

  private final SelenideElement userNameInput = $("input[id='username']");
  private final SelenideElement passwordInput = $("input[id='password']");
  private final SelenideElement passwordSubmitInput = $("input[id='passwordSubmit']");
  private final SelenideElement registerButton = $("button[type='submit']");
  private final SelenideElement successRegistrationTitle = $("p[class='form__subheader']");
  private final SelenideElement errorPasswordMessage = $(".error__password");
  private final SelenideElement userAlreadyExistMessage = $(".form__error.error__username");

  @Step("В поле 'Имя пользователя' ввести '{username}', в поле 'Пароль' значение '{password}' и нажать кнопку 'Зарегистрировать'")
  public RegistrationPage registerNewUser(String userName, String userPassword, String confirmPassword) {
    userNameInput.setValue(userName);
    passwordInput.setValue(userPassword);
    passwordSubmitInput.setValue(confirmPassword);
    registerButton.shouldBe(visible).click();
    return this;
  }

  @Step("Проверить текст уведомления на соответствие значению '{successRegistrationText}'")
  public RegistrationPage checkSuccessRegistrationTitle(String successRegistrationText) {
    successRegistrationTitle.shouldBe(visible).shouldHave(text(successRegistrationText));
    return this;
  }

  @Step("Проверить текст в ошибки на соответствие значению '{errorPasswordText}'")
  public RegistrationPage checkErrorPasswordMessage(String errorPasswordText) {
    errorPasswordMessage.shouldBe(visible).shouldHave(text(errorPasswordText));
    return this;
  }

  @Step("Проверить текст в ошибки на соответствие значению '{userAlreadyExistText}'")
  public RegistrationPage checkUserAlreadyExistMessage(String userAlreadyExistText) {
    userAlreadyExistMessage.shouldBe(visible).shouldHave(text(userAlreadyExistText));
    return this;
  }
}

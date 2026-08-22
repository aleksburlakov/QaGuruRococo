package io.student.rcc.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class RegistrationPage {

  private final SelenideElement userNameInput = $("input[id='username']");
  private final SelenideElement passwordInput = $("input[id='password']");
  private final SelenideElement passwordSubmitInput = $("input[id='passwordSubmit']");
  private final SelenideElement registerButton = $("button[type='submit']");
  private final SelenideElement successRegistrationTitle = $("p[class='form__subheader']");
  private final SelenideElement errorPasswordMessage = $(".error__password");
  private final SelenideElement userAlreadyExistMessage = $(".error__username");

  public RegistrationPage registerNewUser(String userName, String userPassword, String confirmPassword) {
    userNameInput.setValue(userName);
    passwordInput.setValue(userPassword);
    passwordSubmitInput.setValue(confirmPassword);
    registerButton.shouldBe(visible).click();
    return this;
  }

  public RegistrationPage checkSuccessRegistrationTitle(String successRegistrationText) {
    successRegistrationTitle.shouldBe(visible).shouldHave(text(successRegistrationText));
    return this;
  }

  public RegistrationPage checkErrorPasswordMessage(String errorPasswordText) {
    errorPasswordMessage.shouldBe(visible).shouldHave(text(errorPasswordText));
    return this;
  }

  public RegistrationPage checkUserAlreadyExistMessage(String userAlreadyExistText) {
    userAlreadyExistMessage.shouldBe(visible).shouldHave(text(userAlreadyExistText));
    return this;
  }
}

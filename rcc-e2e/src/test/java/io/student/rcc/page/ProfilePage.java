package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

  private final SelenideElement profilePageHeader = $x("//header[text()='Профиль']");
  private final SelenideElement nameInput = $("input[name=\"firstname\"]");
  private final SelenideElement surnameInput = $("input[name=\"surname\"]");
  private final SelenideElement updateProfileBtn = $("button[type=\"submit\"]");

  @Step("Проверить, что страница редактирования профиля открыта")
  public ProfilePage checkProfilePageIsOpened() {
    profilePageHeader.shouldBe(visible);
    return this;
  }

  @Step("Заполнить поле 'Имя' значением '{firstname}'")
  public ProfilePage inputFirstname(String firstname) {
    nameInput.setValue(firstname);
    return this;
  }

  @Step("Заполнить поле 'Фамилия' значением '{surname}'")
  public ProfilePage inputSurname(String surname) {
    surnameInput.setValue(surname);
    return this;
  }

  @Step("Нажать на кнопку '{Обновить профиль}'")
  public MainPage clickUpdateProfile() {
    updateProfileBtn.click();
    return new MainPage();
  }

  @Step("Проверить значение поля 'Имя' на соответствие значению '{firstname}'")
  public ProfilePage checkFirstname(String firstname) {
    nameInput.shouldBe(value(firstname));
    return this;
  }

  @Step("Проверить значение поля 'Фамилия' на соответствие значению '{surname}'")
  public ProfilePage checkSurname(String surname) {
    surnameInput.shouldBe(value(surname));
    return this;
  }
}

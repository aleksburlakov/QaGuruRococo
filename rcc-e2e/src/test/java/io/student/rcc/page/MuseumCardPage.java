package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MuseumCardPage extends BasePage<MuseumCardPage> {

  private final SelenideElement museumCardHeader = $("header[class*='card-header']");
  private final SelenideElement museumCardGeoLocation = $("header[class*='card-header'] + div");
  private final SelenideElement museumCardDescription = $("header[class*='card-header'] + div + div + div");
  private final SelenideElement editButton = $("button[data-testid='edit-museum']");

  @Step("Проверить, что открыта карточка музея '{museumTitle}'")
  public MuseumCardPage checkMuseumCardWithTitleOpened(String museumTitle) {
    museumCardHeader.shouldBe(visible, text(museumTitle));
    return this;
  }

  @Step("Нажать на кнопку 'Редактировать'")
  public AddAndEditMuseumForm clickEditButton() {
    editButton.click();
    return new AddAndEditMuseumForm();
  }

  @Step("Проверить заголовок карточки музея на соответствие значению '{museumTitle}'")
  public MuseumCardPage checkMuseumCardTitle(String museumTitle) {
    museumCardHeader.shouldBe(text(museumTitle));
    return this;
  }

  @Step("Проверить расположение музея на соответствие значениям Страна = '{country}', Город = '{city}'")
  public MuseumCardPage checkMuseumCardGeoLocation(String country, String city) {
    museumCardGeoLocation.shouldBe(text(country + ", \n" + city));
    return this;
  }

  @Step("Проверить описание музея на соответствие значению '{description}'")
  public MuseumCardPage checkMuseumCardDescription(String description) {
    museumCardDescription.shouldBe(text(description));
    return this;
  }
}

package io.student.rcc.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class MuseumsPage extends BasePage<MuseumsPage> {

  private final SelenideElement museumPageHeader = $x("//h2[text()='Музеи']");
  private final SelenideElement addNewMuseumButton = $x("//button[text()='Добавить музей']");
  private final ElementsCollection museumsCards = $$("ul[class*='grid'] li");

  @Step("Проверить, что открыта страница 'Музеи'")
  public MuseumsPage checkMuseumPageIsOpened() {
    museumPageHeader.shouldBe(visible);
    return this;
  }

  @Step("Нажать на кнопку 'Добавить музей'")
  public AddAndEditMuseumForm clickAddNewMuseumButton() {
    addNewMuseumButton.click();
    return new AddAndEditMuseumForm();
  }

  @Step("Проверить наличие музея с названием '{museumTitle}'")
  public MuseumsPage checkMuseumExistByTitle(String museumTitle) {
    getMuseumCardByTitle(museumTitle);
    return this;
  }

  @Step("Кликнуть на карточку музея с названием '{museumTitle}'")
  public MuseumCardPage clickOnMuseumCardByTitle(String museumTitle) {
    getMuseumCardByTitle(museumTitle).click();
    checkMuseumPageIsOpened();
    return new MuseumCardPage();
  }

  private SelenideElement getMuseumCardByTitle(String museumTitle) {
    return museumsCards.stream()
        .filter(museumCard -> museumTitle.equals(museumCard.$("div.mt-2").getText()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Не найден музей с названием " + museumTitle));
  }
}

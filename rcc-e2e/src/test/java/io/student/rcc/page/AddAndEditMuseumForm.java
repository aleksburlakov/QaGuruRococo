package io.student.rcc.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ScrollOptions;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AddAndEditMuseumForm {

  private final SelenideElement addAndEditMuseumModalForm = $("div[data-testid='modal-component']");
  private final SelenideElement editButton = $("button[data-testid='edit-museum']");
  private final SelenideElement addMuseumButton = $("button[type=\"submit\"]");
  private final SelenideElement museumTitleInput = $("input[name='title']");
  private final SelenideElement museumCountrySelect = $("select[name='countryId']");
  private final SelenideElement museumCityInput = $("input[name='city']");
  private final SelenideElement photoMuseumInput = $("input[name='photo']");
  private final SelenideElement museumDescriptionTextarea = $("textarea[name='description']");
  private final SelenideElement errorMuseumTitleField = $("[name='title'] + .text-error-400");
  private final SelenideElement errorMuseumCityField = $("[name='city'] + .text-error-400");
  private final SelenideElement errorMuseumDescriptionField = $("[name='description'] + .text-error-400");

  private final SelenideElement saveButton = $("button[type='submit']");

  @Step("Нажать на кнопку 'Сохранить'")
  public MuseumCardPage clickSaveButton() {
    saveButton.click();
    checkAddNewMuseumFormIsClosed();
    return new MuseumCardPage();
  }

  @Step("Проверить что открыта форма добавления музея")
  public AddAndEditMuseumForm checkAddNewMuseumFormIsOpened() {
    addAndEditMuseumModalForm.shouldBe(visible);
    return this;
  }

  @Step("Проверить что форма добавления музея закрыта")
  public AddAndEditMuseumForm checkAddNewMuseumFormIsClosed() {
    addAndEditMuseumModalForm.shouldBe(not(exist));
    return this;
  }

  @Step("Заполнить поле 'Название музея' значением '{title}'")
  public AddAndEditMuseumForm fillTitle(String title) {
    museumTitleInput.setValue(title);
    return this;
  }

  @Step("Заполнить поле 'Укажите страну' значением '{country}'")
  public AddAndEditMuseumForm fillCountry(String country) {
    ElementsCollection countries = museumCountrySelect.$$("option");
    final int MAX_SCROLL_ITERATOR = 15;
    int scrollIterator = 0;
    while (scrollIterator < MAX_SCROLL_ITERATOR) {
      SelenideElement targetItem = countries.find(text(country));
      if (targetItem.exists()) {
        targetItem.scroll(ScrollOptions.defaultScrollOptions());
        targetItem.click();
        return this;
      }

      museumCountrySelect.scroll(ScrollOptions.defaultScrollOptions());
      scrollIterator++;
    }

    throw new RuntimeException(String.format("Страна с названием '%s' не найдена среди возможных значений поля 'Укажите страну'", country));
  }

  @Step("Заполнить поле 'Укажите город' значением '{city}'")
  public AddAndEditMuseumForm fillCity(String city) {
    museumCityInput.setValue(city);
    return this;
  }

  @Step("Добавить фото музея из ресурса {filePath}")
  public AddAndEditMuseumForm addMuseumPhoto(String filePath) {
    photoMuseumInput.uploadFromClasspath(filePath);
    return this;
  }

  @Step("Заполнить поле 'О музее' значением '{description}'")
  public AddAndEditMuseumForm fillDescription(String description) {
    museumDescriptionTextarea.setValue(description);
    return this;
  }

  @Step("Нажать на кнопку 'Добавить'")
  public MuseumsPage clickAddMuseumButton() {
    addMuseumButton.click();
    checkAddNewMuseumFormIsClosed();
    return new MuseumsPage();
  }

  @Step("Нажать на кнопку 'Добавить' и проверить соответствие текста ошибки у поля 'Название музея' значению '{errorText}'")
  public AddAndEditMuseumForm clickSaveAndCheckErrorMuseumTitleText(String errorText) {
    addMuseumButton.click();
    errorMuseumTitleField.shouldBe(text(errorText));
    checkAddNewMuseumFormIsOpened();
    return this;
  }

  @Step("Нажать на кнопку 'Добавить' и проверить соответствие текста ошибки у поля 'Укажите город' значению '{errorText}'")
  public AddAndEditMuseumForm clickSaveAndCheckErrorMuseumCityText(String errorText) {
    addMuseumButton.click();
    errorMuseumCityField.shouldBe(text(errorText));
    checkAddNewMuseumFormIsOpened();
    return this;
  }

  @Step("Нажать на кнопку 'Добавить' и проверить соответствие текста ошибки у поля 'О музее' значению '{errorText}'")
  public AddAndEditMuseumForm clickSaveAndCheckErrorMuseumDescriptionText(String errorText) {
    addMuseumButton.click();
    errorMuseumDescriptionField.shouldBe(text(errorText));
    checkAddNewMuseumFormIsOpened();
    return this;
  }

}

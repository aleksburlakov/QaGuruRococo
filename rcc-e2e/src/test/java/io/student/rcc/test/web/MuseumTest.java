package io.student.rcc.test.web;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.jupiter.extension.TestDataExtension;
import io.student.rcc.model.TestData;
import io.student.rcc.page.MainPage;
import io.student.rcc.service.MuseumClient;
import io.student.rcc.service.MuseumDbClient;
import io.student.rcc.utils.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith({TestDataExtension.class, BrowserExtension.class})
public class MuseumTest {

  private static final Config CFG = Config.getInstance();
  private final List<UUID> createdMuseums = new ArrayList<>();
  MuseumClient museumClient = new MuseumDbClient();

  @AfterEach
  public void deleteCreatedMuseum() {
    if (!createdMuseums.isEmpty()) {
      UUID museumId = createdMuseums.getFirst();
      museumClient.removeMuseum(museumClient.findMuseumById(museumId).get());
    }
  }

  @Test
  @User
  @DisplayName("Создание музея")
  public void createMuseumTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkPopupText("Добавлен музей: " + museumTitle)
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }

  @Test
  @User
  @Museum(title = "Старое название", city = "Минск", countryName = "Белоруссия", description = "Старое описание")
  @DisplayName("Редактирование музея")
  public void editMuseumTest(TestData testData) {
    String newMuseumTitle = "Новое название";
    String newMuseumCountry = "Россия";
    String newMuseumCity = "Москва";
    String newMuseumDescription = "Новое описание";
    String newPhotoPath = "files/pushkin_musei.jpg";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickOnMuseumCardByTitle(testData.museum().title())
        .clickEditButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(newMuseumTitle)
        .fillCountry(newMuseumCountry)
        .fillCity(newMuseumCity)
        .fillDescription(newMuseumDescription)
        .addMuseumPhoto(newPhotoPath)
        .clickSaveButton()
        .checkPopupText("Обновлен музей: " + newMuseumTitle)
        .checkMuseumCardTitle(newMuseumTitle)
        .checkMuseumCardGeoLocation(newMuseumCountry, newMuseumCity)
        .checkMuseumCardDescription(newMuseumDescription);
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Название музея' короче минимального значения")
  public void createMuseumWithVeryShortTitleTest(TestData testData) {
    String museumTitle = "mt";
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickSaveAndCheckErrorMuseumTitleText("Название не может быть короче 3 символов");
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Название музея' длинее максимального значения")
  public void createMuseumWithVeryLongTitleTest(TestData testData) {
    String museumTitle = TestDataGenerator.generateStringWithExpectedLength(256);
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickSaveAndCheckErrorMuseumTitleText("Название не может быть длиннее 255 символов");
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Название музея' равным минимальному значению")
  public void createMuseumWithShortTitleTest(TestData testData) {
    String museumTitle = TestDataGenerator.generateStringWithExpectedLength(3);
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Название музея' равным максимальному значению")
  public void createMuseumWithLongTitleTest(TestData testData) {
    String museumTitle = TestDataGenerator.generateStringWithExpectedLength(255);
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Укажите город' короче минимального значения")
  public void createMuseumWithVeryShortCityTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = TestDataGenerator.generateStringWithExpectedLength(2);
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickSaveAndCheckErrorMuseumCityText("Город не может быть короче 3 символов");
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Укажите город' длинее максимального значения")
  public void createMuseumWithVeryLongCityTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = TestDataGenerator.generateStringWithExpectedLength(256);
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickSaveAndCheckErrorMuseumCityText("Город не может быть длиннее 255 символов");
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Укажите город' равным минимальному значению")
  public void createMuseumWithShortCityTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = TestDataGenerator.generateStringWithExpectedLength(3);
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'Укажите город' равным максимальному значению")
  public void createMuseumWithLongCityTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = TestDataGenerator.generateStringWithExpectedLength(255);
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDescription";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'О музее' короче минимального значения")
  public void createMuseumWithTooShortDescriptionTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = "MuseumDesc";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickSaveAndCheckErrorMuseumDescriptionText("Описание не может быть короче 10 символов");
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'О музее' длинее максимального значения")
  public void createMuseumWithTooLongDescriptionTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = TestDataGenerator.generateStringWithExpectedLength(1001);
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickSaveAndCheckErrorMuseumDescriptionText("Описание не может быть длиннее 1000 символов");
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'О музее' равным минимальному значения")
  public void createMuseumWithShortDescriptionTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = TestDataGenerator.generateStringWithExpectedLength(11);
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }

  @Test
  @User
  @DisplayName("Создание музея cо значением поля 'О музее' равным максимальному значения")
  public void createMuseumWithLongDescriptionTest(TestData testData) {
    String museumTitle = "MuseumTitle";
    String country = "Россия";
    String city = "Москва";
    String photoPath = "files/pushkin_musei.jpg";
    String museumDescription = TestDataGenerator.generateStringWithExpectedLength(1000);
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickMuseumLink()
        .checkMuseumPageIsOpened()
        .clickAddNewMuseumButton()
        .checkAddNewMuseumFormIsOpened()
        .fillTitle(museumTitle)
        .fillCountry(country)
        .fillCity(city)
        .addMuseumPhoto(photoPath)
        .fillDescription(museumDescription)
        .clickAddMuseumButton()
        .checkMuseumExistByTitle(museumTitle);
    createdMuseums.add(museumClient.findMuseumByTitle(museumTitle).get().id());
  }
}

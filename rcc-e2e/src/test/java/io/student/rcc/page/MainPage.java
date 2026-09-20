package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

  private final SelenideElement enterButton = $x("//button[text()='Войти']");
  private final SelenideElement addPaintingLink = $("a[href='/painting']");
  private final SelenideElement addArtistLink = $("a[href='/artist']");
  private final SelenideElement addMuseumLink = $("a[href='/museum']");
  private final SelenideElement profileAvatar = $($("figure[class*='avatar']"));
  private final SelenideElement popup = $("div.text-base");


  @Step("Нажать на кнопку 'Войти'")
  public AuthorizationPage clickLoginButton() {
    enterButton.click();
    return new AuthorizationPage();
  }

  @Step("Проверить, что открыта главная страница")
  public MainPage checkMainPageDisplayed() {
    addPaintingLink.shouldBe(visible);
    addArtistLink.shouldBe(visible);
    addMuseumLink.shouldBe(visible);
    return this;
  }

  @Step("Нажать на аватар профиля")
  public ProfilePage clickProfileAvatarButton() {
    profileAvatar.click();
    return new ProfilePage();
  }

  @Step("Нажать ссылку 'Музеи'")
  public MuseumsPage clickMuseumLink() {
    addMuseumLink.click();
    return new MuseumsPage();
  }

  @Step("Проверить текст уведомления на соответствие значению '{text}'")
  public MainPage checkPopupText(String text) {
    popup.shouldHave(text(text));
    return this;
  }
}

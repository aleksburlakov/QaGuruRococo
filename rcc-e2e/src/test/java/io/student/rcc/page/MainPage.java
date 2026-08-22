package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class MainPage {

  private final SelenideElement enterButton = $x("//button[text()='Войти']");
  private final SelenideElement addPaintingLink = $("a[href='/painting']");
  private final SelenideElement addArtistLink = $("a[href='/artist']");
  private final SelenideElement addMuseumLink = $("a[href='/museum']");

  public AuthorizationPage clickLoginButton() {
    enterButton.click();
    return new AuthorizationPage();
  }

  public void checkMainPageDisplayed() {
    addPaintingLink.shouldBe(visible);
    addArtistLink.shouldBe(visible);
    addMuseumLink.shouldBe(visible);
  }
}

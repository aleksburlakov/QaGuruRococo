package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class MainPage {

  private final SelenideElement enterButton = $x("//button[text()='Войти']");
  private final SelenideElement addPaintingButton = $("a[href='/painting']");
  private final SelenideElement addArtistButton = $("a[href='/artist']");
  private final SelenideElement addMuseumButton = $("a[href='/museum']");

  public AuthorizationPage clickLoginButton() {
    enterButton.click();
    return new AuthorizationPage();
  }

  public void checkMainPageDisplayed() {
    addPaintingButton.shouldBe(visible);
    addArtistButton.shouldBe(visible);
    addMuseumButton.shouldBe(visible);
  }
}

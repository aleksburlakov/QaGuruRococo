package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

  private final SelenideElement popup = $("div.text-base");

  @Step("Проверить текст всплавающего сообщения на соответствие значению '{popupText}'")
  public T checkPopupText(String popupText) {
    popup.shouldHave(text(popupText));
    return (T) this;
  }
}

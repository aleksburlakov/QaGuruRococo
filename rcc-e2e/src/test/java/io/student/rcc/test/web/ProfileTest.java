package io.student.rcc.test.web;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.jupiter.extension.TestDataExtension;
import io.student.rcc.model.TestData;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({TestDataExtension.class, BrowserExtension.class})
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @User
  @DisplayName("Редактирование профиля пользователя")
  public void editProfileTest(TestData testData) {
    String newFirstname = "NewFirstname";
    String newSurname = "NewSurname";
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickLoginButton()
        .login(testData.user().username(), testData.userPassword())
        .checkMainPageDisplayed()
        .clickProfileAvatarButton()
        .checkProfilePageIsOpened()
        .inputFirstname(newFirstname)
        .inputSurname(newSurname)
        .clickUpdateProfile()
        .checkPopupText("Профиль обновлен")
        .clickProfileAvatarButton()
        .checkProfilePageIsOpened()
        .checkFirstname(newFirstname)
        .checkSurname(newSurname);
  }
}

package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.startMaximized = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();

        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button.button").click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with empty login")
    void shouldGetErrorIfEmptyLogin() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();

        $("[data-test-id=login] span.input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with empty password")
    void shouldGetErrorIfEmptyPassword() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("button.button").click();

        $("[data-test-id=password] span.input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.visible);
    }
}
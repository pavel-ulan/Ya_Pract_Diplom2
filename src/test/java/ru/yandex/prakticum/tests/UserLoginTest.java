package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.UserClient;
import ru.yandex.prakticum.UserGenerate;
import ru.yandex.prakticum.model.UserCredentials;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class UserLoginTest extends BaseScenario {
    private UserCredentials userCredentials;

    @Before
    @Description("Data initialization")
    public void tearUp() {
        user = UserGenerate.getRandomUser();
        token = UserClient.create(user).then().extract().path("accessToken");
    }

    @After
    @Description("Data clean")
    public void tearDown() {
        if (token != null) UserClient.delete(token);
    }

    @Test
    @DisplayName("Логин пользователя")
    @Description("Логин валидной учетной записи")
    public void loginUserTest() {
        userCredentials = UserCredentials.from(user);
        UserClient.login(userCredentials)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Логин пользователя c неправильным логином")
    @Description("Логин некорректной учетной записи")
    public void loginUserIncorrectEmailTest() {
        userCredentials = UserCredentials.withIncorrectEmail(user);
        UserClient.login(userCredentials)
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя c неправильным паролем")
    @Description("Логин некорректной учетной записи")
    public void loginUserIncorrectPasswordTest() {
        userCredentials = UserCredentials.withIncorrectPassword(user);
        UserClient.login(userCredentials)
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
}

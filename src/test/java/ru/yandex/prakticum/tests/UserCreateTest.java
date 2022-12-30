package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.UserClient;
import ru.yandex.prakticum.UserGenerate;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;

public class UserCreateTest extends BaseScenario {

    @Before
    @Description("Data initialization")
    public void tearUp() {
        user = UserGenerate.getRandomUser();
    }

    @After
    @Description("Data clean")
    public void tearDown() {
        if (token != null) UserClient.delete(token);
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @Description("Создание нового уникального пользователя")
    public void createNewUserTest() {
        token = UserClient.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя который уже зарегестрирован")
    @Description("Создание нового пользователя и его повторная регестрация")
    public void createDuplicateUserTest() {
        token = UserClient.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
        UserClient.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Создание нового пользователя без поля name")
    public void createUserWithoutNameTest() {
        user = UserGenerate.getUserWithoutName();
        UserClient.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Создание нового пользователя без поля email")
    public void createUserWithoutEmailTest() {
        user = UserGenerate.getUserWithoutEmail();
        UserClient.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }


    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Создание нового пользователя без поля password")
    public void createUserWithoutPasswordTest() {
        user = UserGenerate.getUserWithoutPassword();
        UserClient.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }
}

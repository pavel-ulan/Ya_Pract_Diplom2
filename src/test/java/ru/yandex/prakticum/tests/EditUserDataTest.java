package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.UserClient;
import ru.yandex.prakticum.UserGenerate;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class EditUserDataTest extends BaseScenario {

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
    @DisplayName("Редактирование авторизованного пользователя")
    @Description("Изменение данных авторизованного пользователя")
    public void editUserAuthorizationTest() {
        UserClient.edit(UserGenerate.getUserWithModified(), token)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true))
                .body("user.email", is("testuser@yandex.ru"))
                .body("user.name", is("User"));
    }

    @Test
    @DisplayName("Редактирование неавторизованного пользователя")
    @Description("Редактирование полей пользователя")
    public void editUserNotAuthorizationTest() {
        UserClient.editNotAuthorization(UserGenerate.getUserWithModified())
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}

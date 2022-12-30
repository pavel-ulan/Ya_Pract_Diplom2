package ru.yandex.prakticum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.prakticum.model.User;
import ru.yandex.prakticum.model.UserCredentials;

import static io.restassured.RestAssured.given;
import static ru.yandex.prakticum.OrderClient.AUTH;
import static ru.yandex.prakticum.constant.Endpoints.*;

public class UserClient {

    @Step("Создание нового пользователя")
    public static Response create(User user) {
        return given()
                .body(user)
                .when()
                .post(REGISTER);
    }

    @Step("Удаление пользователя")
    public static Response delete(String accessToken) {
        return given()
                .header(AUTH, accessToken)
                .body("")
                .when()
                .delete(PATH);
    }

    @Step("Логин пользователя")
    public static Response login(UserCredentials userCredentials) {
        return given()
                .body(userCredentials)
                .when()
                .post(LOGIN);

    }

    @Step("Изменение данных пользователя c авторизацией")
    public static Response edit(User user, String token) {
        return given()
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(PATH);
    }

    @Step("Изменение данных пользователя c авторизацией")
    public static Response editNotAuthorization(User user) {
        return given()
                .body(user)
                .when()
                .patch(PATH);
    }
}

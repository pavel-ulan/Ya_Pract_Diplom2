package ru.yandex.prakticum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.prakticum.BaseScenario;
import ru.yandex.prakticum.OrderClient;
import ru.yandex.prakticum.UserClient;
import ru.yandex.prakticum.UserGenerate;
import ru.yandex.prakticum.model.Order;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class GetOrderTest extends BaseScenario {

    @Before
    @Description("Data initialization")
    public void tearUp() {
        user = UserGenerate.getRandomUser();
        token = UserClient.create(user).then().extract().path("accessToken");
    }

    @After
    @Description("Data clean")
    public void tearDown() {
        if (token!=null) UserClient.delete(token);
    }

    @Test
    @DisplayName("Успешное получени заказа пользователя")
    @Description("Получение заказа авторизованного пользователя")
    public void getUserOrdersWithAuthTest() {
        ingredients = Order.makeIngredients();
        OrderClient.create(ingredients, token);
        OrderClient.getOrdersWithAuth(token)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение заказа пользователя без авторизации")
    @Description("Ошибка получения заказа неавторизованного пользователя")
    public void getUserOrdersWithoutAuthTest() {
        OrderClient.getOrdersWithoutAuth()
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}

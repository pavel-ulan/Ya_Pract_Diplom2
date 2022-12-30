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

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest extends BaseScenario {

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
    @DisplayName("Заказ авторизованного пользователя")
    @Description("Заказ с валидными ингредиентами и авторизацией")
    public void createOrderWithAuthAndIngredientsPositiveTest() {
        ingredients = Order.makeIngredients();
        OrderClient.create(ingredients, token)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Заказ без авторизации")
    @Description("Заказ с валидными ингредиентами без авторизации")
    public void createOrderWithoutAuthTest() {
        ingredients = Order.makeIngredients();
        OrderClient.createWithoutAuth(ingredients)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Заказ без ингредиентов")
    @Description("Заказ с авторизацией без ингридиентов")
    public void createOrderWithoutIngredientTest() {
        OrderClient.createWithAuthWithoutIngredient(token)
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Заказ с невалидным хэшем ингредиента")
    @Description("Заказ с авторизацией и невалидным хэшом ингридиентов")
    public void createOrderWithIncorrectIngredientTest() {
        ingredients = Order.makeIngredientsWithWrongHash();
        OrderClient.createWithAuthWithIncorrectHashIngredient(ingredients, token)
                .then()
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }
}

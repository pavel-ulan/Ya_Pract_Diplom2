package ru.yandex.prakticum;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.prakticum.model.User;

public class UserGenerate {

    public static User getRandomUser() {
        return new User(
                RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static User getUserWithoutEmail() {
        return new User(
                "",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static User getUserWithoutName() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru",
                "",
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static User getUserWithoutPassword() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10),
                ""
        );
    }

    public static User getUserWithModified() {
        return new User("testuser@yandex.ru", "User", "password");
    }
}

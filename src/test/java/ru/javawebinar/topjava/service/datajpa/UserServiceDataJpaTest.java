package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() {
        User user = service.getWithMeals(100000);
        assertMatch(user, USER);
        assertMatch(user.getMeals(), MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6);
    }

    @Test
    public void getUserWithEmptyMeals() {
        User newUser = service.create(new User(null, "User1", "user1@yandex.ru", "password1", Role.ROLE_USER));
        User user = service.getWithMeals(newUser.getId());
        assertMatch(user, newUser);
        assertMatch(user.getMeals(), List.of());
    }
}
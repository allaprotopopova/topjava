package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal meal = service.getWithUser(MEAL1.getId(), USER.getId());
        assertMatch(meal, MEAL1);
        assertMatch(meal.getUser(), USER);
    }

}

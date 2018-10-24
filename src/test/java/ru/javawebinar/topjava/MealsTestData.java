package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealsTestData {

    public static final Meal MEAL_USER_100002 = new Meal(START_SEQ + 2, LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_USER_100003 = new Meal(START_SEQ + 3, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_USER_100004 = new Meal(START_SEQ + 4, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 500);

    public static void assertMatch(List<Meal> actual, Meal... expect) {
        List<Meal> expected = Arrays.asList(expect);
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}

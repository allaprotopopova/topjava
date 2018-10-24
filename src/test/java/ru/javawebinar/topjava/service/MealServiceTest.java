package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealsTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_USER_100002.getId(), USER_ID);
        assertMatch(meal, MEAL_USER_100002);
    }

    @Test(expected = NotFoundException.class)
    public void getMealNotExist() {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getMealNotBelongToUser() {
        service.get(MEAL_USER_100002.getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL_USER_100002.getId(), USER_ID);
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL_USER_100004, MEAL_USER_100003 );
    }

    @Test(expected = NotFoundException.class)
    public void deleteMealNotExist() {
        service.delete(MEAL_USER_100002.getId() + 10, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteMealNotBelongToUser() {
        service.delete(MEAL_USER_100002.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 29), LocalDate.of(2015, Month.MAY, 30), USER_ID);
        assertMatch(meals, MEAL_USER_100003, MEAL_USER_100002);
    }

    @Test
    public void getBetweenDatesReturnEmpty() {
        List<Meal> meals = service.getBetweenDates(LocalDate.of(2018, Month.MAY, 29), LocalDate.of(2018, Month.MAY, 30), USER_ID);
        Assert.assertEquals(Collections.emptyList(), meals);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 29, 10, 0), LocalDateTime.of(2015, Month.MAY, 29, 11, 0), USER_ID);
        assertMatch(meals, MEAL_USER_100002);
    }

    @Test
    public void getBetweenDateTimesReturnEmpty() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 29, 11, 0), LocalDateTime.of(2015, Month.MAY, 29, 12, 0), USER_ID);
        Assert.assertEquals(Collections.emptyList(), meals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL_USER_100004, MEAL_USER_100003, MEAL_USER_100002);

    }

    @Test
    public void getAllUserHaveNotMeal() {
        List<Meal> meals = service.getAll(100002);
        Assert.assertEquals(Collections.emptyList(), meals);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL_USER_100002);
        updated.setDateTime(LocalDateTime.of(2018, Month.MAY, 29, 11, 0));
        updated.setCalories(2345);
        updated.setDescription("updated");
        service.update(updated, USER_ID);
        assertMatch(updated, service.get(MEAL_USER_100002.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateMealNotExist() {
        Meal updated = new Meal(1, LocalDateTime.of(2018, Month.MAY, 29, 11, 0), "updated", 1234);
        service.update(updated, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateMealNotBelogToUser() {
        Meal updated = new Meal(MEAL_USER_100002);
        updated.setDateTime(LocalDateTime.of(2018, Month.MAY, 29, 11, 0));
        updated.setCalories(2345);
        updated.setDescription("updated");
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2018, Month.MAY, 29, 11, 0), "updated", 1234);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(service.get(created.getId(), USER_ID), created);
        assertMatch(meals, newMeal, MEAL_USER_100004, MEAL_USER_100003, MEAL_USER_100002);
    }
}
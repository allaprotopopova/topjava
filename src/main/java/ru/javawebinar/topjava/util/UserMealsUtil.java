package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class UserMealsUtil {
  public static void main(String[] args) {
    List<UserMeal> mealList = Arrays.asList(
        new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
        new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
        new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
        new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
        new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
        new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    );
    getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    getFilteredWithExceededOptional1(mealList, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);

  }

  public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

    Map<LocalDate, Integer> caloriesPerDays = new HashMap<>();

    mealList.forEach(userMeal -> caloriesPerDays.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum));

    List<UserMealWithExceed> result = new ArrayList<>();
    mealList.forEach(userMeal -> {
      if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
        result.add(new UserMealWithExceed(userMeal.getDateTime(),
                                           userMeal.getDescription(),
                                             userMeal.getCalories(),
                                                caloriesPerDays.get(
                                                    userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
      }
    });
    return result;
  }

  public static List<UserMealWithExceed> getFilteredWithExceededOptional1(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

    Map<LocalDate, Integer> caloriesPerDays = mealList.stream()
        .collect(Collectors
            .groupingBy(meal -> meal.getDateTime().toLocalDate(),
                              Collectors.summingInt(UserMeal::getCalories)));

    return mealList.stream()
        .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
        .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesPerDays.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
        .collect(Collectors.toList());

  }

}

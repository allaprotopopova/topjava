package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.putIfAbsent(userId, new ConcurrentHashMap<>());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal.getUserId() == userId ? meal : oldMeal);
    }

    @Override
    public boolean delete(int id, int userId) {

        Meal meal = repository.get(userId) == null ? null : repository.get(userId).get(id);

        if (meal != null && meal.getUserId() == userId) {
            meal = repository.get(userId).remove(id);
            return meal != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(userId) == null ? null : repository.get(userId).get(id);
        return (meal == null || meal.getUserId() != userId) ? null : meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterBy(userId, meal -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate dateFrom, LocalDate dateTill) {
        Predicate<Meal> filter = meal -> DateTimeUtil.isBetween(meal.getDate(), dateFrom, dateTill);
        return filterBy(userId, filter);
    }


    private List<Meal> filterBy(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return new ArrayList<>();
        }
        return meals.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());

    }
}


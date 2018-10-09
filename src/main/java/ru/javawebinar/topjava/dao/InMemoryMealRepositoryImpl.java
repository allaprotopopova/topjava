package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private AtomicInteger idGenerator = new AtomicInteger(0);
    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public InMemoryMealRepositoryImpl() {
        initialiseMeal();
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal update(Meal meal) {
        return meals.computeIfPresent(meal.getId(), (k, v) -> meal);
    }

    @Override
    public boolean delete(int id) {
        Meal meal = meals.remove(id);
        return meal != null;
    }

    @Override
    public Meal add(Meal newMeal) {
        newMeal.setId(idGenerator.incrementAndGet());
        meals.put(newMeal.getId(), newMeal);
        return newMeal;
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }

    private void initialiseMeal() {
        add(new Meal(idGenerator.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(idGenerator.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(idGenerator.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(idGenerator.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(idGenerator.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(idGenerator.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }
}

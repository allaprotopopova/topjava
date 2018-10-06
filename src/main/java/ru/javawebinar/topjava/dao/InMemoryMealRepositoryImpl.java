package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private  static ConcurrentHashMap<Integer, Meal> meals = initialiseMeal();

    private static ConcurrentHashMap<Integer, Meal> initialiseMeal() {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500, idGenerator.incrementAndGet()),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000, idGenerator.incrementAndGet()),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500, idGenerator.incrementAndGet()),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000, idGenerator.incrementAndGet()),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500, idGenerator.incrementAndGet()),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510, idGenerator.incrementAndGet())
        );
        ConcurrentHashMap<Integer, Meal> result = new ConcurrentHashMap<>();
        mealList.forEach(meal -> result.putIfAbsent(meal.getId(), meal));
        return result;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal update(Meal meal) {
        return meals.computeIfPresent(meal.getId(), (k, v)-> meal);
    }

    @Override
    public boolean delete(int id) {
        meals.remove(id);
        return true;
    }

    @Override
    public Meal add(Meal newMeal) {
        newMeal.setId(idGenerator.incrementAndGet());
        return  meals.putIfAbsent(newMeal.getId(), newMeal);
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }
}

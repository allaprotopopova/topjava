package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = repository.computeIfPresent(id, (k, v) -> v.getUserId() == userId ? null : v);
        return meal == null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return (meal == null || meal.getUserId() != userId) ? null : meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> meals = repository.values().stream().filter(meal -> meal.getUserId() == userId).sorted((m1, m2) -> {
            return m2.getDate().compareTo(m1.getDate()) != 0 ? m2.getDate().compareTo(m1.getDate()) : m2.getTime().compareTo(m1.getTime());
        }).collect(Collectors.toList());
        return meals;

    }
}


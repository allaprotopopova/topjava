package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {

    List<Meal> getAll();
    Meal update(Meal id);
    boolean delete(int id);
    Meal add(Meal newMeal);

    Meal getById(int id);
}

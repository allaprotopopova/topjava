package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal update(int userId, Meal meal) {
        return checkNotFoundWithId(repository.save(userId, meal), userId);
    }

    @Override
    public Meal create(int userId, Meal meal) {
        return checkNotFoundWithId(repository.save(userId, meal), userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public List<MealWithExceed> getAll(int userId) {
        return MealsUtil.getWithExceeded(repository.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    @Override
    public List<MealWithExceed> getFiltered(LocalDate dateF, LocalDate dateT, LocalTime timeF, LocalTime timeT, int userId) {
        LocalDate dateFrom = dateF == null ? LocalDate.MIN : dateF;
        LocalDate dateTill = dateT == null ? LocalDate.MAX : dateT;
        LocalTime timeFrom = timeF == null ? LocalTime.MIN : timeF;
        LocalTime timeTill = timeT == null ? LocalTime.MAX : timeT;
        return MealsUtil.getFilteredWithExceeded(repository.getFiltered(userId, dateFrom, dateTill), SecurityUtil.authUserCaloriesPerDay(), meal -> DateTimeUtil.isBetween(meal.getTime(), timeFrom, timeTill));
    }
}
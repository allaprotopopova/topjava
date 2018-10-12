package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class MealRestController {

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        return service.save(meal);
    }

    public void delete(int id, int userId) throws NotFoundException {
        service.delete(id, userId);
    }

    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(service.get(id, userId), id);
    }

    public List<MealWithExceed> getAll(int userId) {
        return service.getAll(userId);
    }

}
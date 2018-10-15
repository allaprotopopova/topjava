package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal, Integer id) {
        ValidationUtil.assureIdConsistent(meal, id);
        return service.save(SecurityUtil.authUserId(), meal);
    }

    public void delete(Integer id) throws NotFoundException {
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(Integer id) throws NotFoundException {
        return service.get(id, SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getAll() {
        return service.getAll(SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getBetween(LocalDate dateFrom, LocalDate dateTill, LocalTime timeFrom, LocalTime timeTill) {

        return service.getFiltered(dateFrom, dateTill, timeFrom, timeTill, SecurityUtil.authUserId());
    }
}
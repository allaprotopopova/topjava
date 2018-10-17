package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal update(Meal meal, Integer id) {
        log.info("update {} with id={}", meal, id);
        meal.setUserId(SecurityUtil.authUserId());
        assureIdConsistent(meal, id);
        return service.update(SecurityUtil.authUserId(), meal);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        return service.create(SecurityUtil.authUserId(), meal);
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
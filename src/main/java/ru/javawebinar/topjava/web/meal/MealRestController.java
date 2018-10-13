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

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class MealRestController {

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal, int id) {
        ValidationUtil.assureIdConsistent(meal, id);
        return service.save(meal);
    }

    public void delete(int id) throws NotFoundException {
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) throws NotFoundException {
        return checkNotFoundWithId(service.get(id, SecurityUtil.authUserId()), id);
    }

    public List<MealWithExceed> getAll() {
        return service.getAll(SecurityUtil.authUserId());
    }

    public List<MealWithExceed> filter(String dateF, String dateT, String timeF, String timeT) {

        LocalDate dateFrom = dateF.isEmpty() ? LocalDate.MIN : LocalDate.parse(dateF);
        LocalDate dateTill = dateT.isEmpty() ? LocalDate.MAX : LocalDate.parse(dateT);
        LocalTime timeFrom = timeF.isEmpty() ? LocalTime.MIN : LocalTime.parse(timeF);
        LocalTime timeTill = timeT.isEmpty() ? LocalTime.MAX : LocalTime.parse(timeT);

        return service.getFiltered(dateFrom, dateTill, timeFrom, timeTill, SecurityUtil.authUserId());
    }
}
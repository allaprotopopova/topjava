package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.dao.MealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    @SuppressWarnings("unused")
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository dao;

    @Override
    public void init() {
        dao = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "delete":
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                dao.delete(mealId);
                response.sendRedirect("meals");
                break;
            case "update":
                Integer id = Integer.valueOf(request.getParameter("mealId"));
                Meal meal = dao.getById(id);
                request.setAttribute("meal", meal);
            case "insert":
                request.getRequestDispatcher("editmeal.jsp").forward(request, response);
                break;
            case "get":
            default:
                request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
                request.setAttribute("formatter", TimeUtil.formatter);
                request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("mealId"));
        } catch (NumberFormatException ignored) {

        }

        String description = req.getParameter("description");

        int calories = 0;
        try {
            calories = Integer.parseInt(req.getParameter("calories"));
        } catch (NumberFormatException e) {
            log.debug("insert meal wrong calories format");
        }

        LocalDateTime date = null;
        try {
            date = LocalDateTime.parse(req.getParameter("dateTime"));
        } catch (DateTimeException e) {
            log.debug("insert meal wrong dateTime format");
        }

        Meal meal = new Meal(id, date, description, calories);
        if (id == 0) {
            dao.add(meal);
        } else {
            meal.setId(id);
            dao.update(meal);
        }

        req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        req.setAttribute("formatter", TimeUtil.formatter);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }
}

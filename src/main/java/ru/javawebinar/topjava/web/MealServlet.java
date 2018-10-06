package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.dao.MealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

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
    private static final long serialVersionUID = 5516815470180527248L;
    private static final MealRepository dao = new InMemoryMealRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("get".equals(action) || action==null) {
            request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
            request.setAttribute("formatter", MealsUtil.formatter);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
        if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            dao.delete(userId);
            response.sendRedirect("meals");
        }
        if ("update".equals(action) || "insert".equals(action)) {
            if ("update".equals(action)) {
                Integer id = Integer.valueOf(request.getParameter("userId"));
                Meal meal = dao.getById(id);
                request.setAttribute("meal", meal);
            }

            request.getRequestDispatcher("editmeal.jsp").forward(request, response);
        }




    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Meal meal = new Meal();
        req.setCharacterEncoding("UTF-8");
        meal.setDescription(req.getParameter("description"));
        try {
            int calories = Integer.parseInt(req.getParameter("calories"));
            meal.setCalories(calories);
        } catch (NumberFormatException e) {
            log.debug("insert meal wrong calories format");
        }
        try {
            LocalDateTime date = LocalDateTime.parse(req.getParameter("dateTime"));
            meal.setDateTime(date);
        } catch (DateTimeException e) {
            log.debug("insert meal wrong dateTime format");

        }

            String id = req.getParameter("mealId");
        if (id==null || id.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
        }
        req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        req.setAttribute("formatter", MealsUtil.formatter);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);


    }
}

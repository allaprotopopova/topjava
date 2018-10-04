package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    @SuppressWarnings("unused")
    private static final Logger log = getLogger(MealServlet.class);
    private static final long serialVersionUID = 5516815470180527248L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        log.debug("redirect to users");
        List<MealWithExceed> mealsList = MealsUtil.getFilteredWithExceeded(MealsUtil.meals, LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", mealsList);
        request.setAttribute("formatter", MealsUtil.formatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);

    }
}

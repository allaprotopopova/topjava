<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Meal</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            width: 40%;
        }

        tr {
            text-align: center;
        }

    </style>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<form method="post" action="meals" name=frmMeal>
    <input type="hidden" readonly="readonly" name="mealId"
           value="<c:out value="${meal.getId()}"/>"/><br/>
    DATE : <input type="datetime-local" name="dateTime" value="<c:out value="${meal.getDateTime()}"/>"/><br/>
    DESCRIPTION : <input type="text" name="description"
                         value="<c:out value="${meal.getDescription()}"/>"/><br/>
    CALORIES : <input type="text" name="calories"
                      value="<c:out value="${meal.getCalories()}"/>"/><br/>
    <input type="submit" value="Submit"/>

</form>
</body>
</html>

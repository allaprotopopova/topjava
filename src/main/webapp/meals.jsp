<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Meals</title>
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
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <c:set var="meals" value="${requestScope.get('meals')}"/>
    <c:set var="formatter" value="${requestScope.get('formatter')}"/>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.isExceed() ? 'red' : 'green'}">
            <td>${formatter.format(meal.getDateTime())}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td><a href="meals?action=update&mealId=${meal.getId()}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${meal.getId()}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<h3><a href="meals?action=insert">Add meal</a></h3>
</body>
</html>

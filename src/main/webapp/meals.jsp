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
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:set var="meals" value="${requestScope.get('meals')}"/>
    <c:set var="formatter" value="${requestScope.get('formatter')}"/>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.isExceed() ? 'red' : 'green'}">
            <td>${formatter.format(meal.getDateTime())}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

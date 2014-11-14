<%@ page import="ru.levelp.beans.BeanExample" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Таблица умножения</title>
</head>
<body>

<%
    BeanExample example = new BeanExample();
    example.setPassword("PASSWORD");
%>

<%="Данные из JavaBean = " + example.getPassword() %>
<br>

<span>${html}</span>

<h2>Таблица умножения</h2>

<% int size = Integer.valueOf(String.valueOf(request.getAttribute("size"))); %>
Размер таблицы = <%=size %>

<table border="1">
    <% for (int i = 1; i <= size; ++i) { /* Цикл по строчкам */ %>
    <tr>
        <% for (int j = 1; j <= size; ++j) { /* Цикл по столбцам */ %>
        <td>
            <%=(i * j)%>
        </td>
        <% } %>
    </tr>
    <% } %>
</table>

</body>
</html>

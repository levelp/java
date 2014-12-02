<%@ page import="calc.Calc" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Калькулятор</title>
</head>
<body>

<form action="index.jsp" method="get">
    <label>
        A:
        <input name="a" type="text" value="${param.a}"/>
    </label>
    <label>
        B:
        <input name="b" type="text" value="${param.b}"/>
    </label>
    <br>
    <input type="submit" value="Сложить" name="sum"/>
    <input type="submit" value="Умножить" name="mul"/>
</form>

<hr>
<%
    Calc calc = new Calc();
    if (request.getParameter("sum") != null) {
        out.write("<b>Сумма:</b> " + calc.sum(request.getParameter("a"), request.getParameter("b")));
    }
    if (request.getParameter("mul") != null) {
%>
<b>Произведение:</b> <%= calc.mul(request.getParameter("a"), request.getParameter("b")) %>
<%
    }
%>

<hr>
Используя JSP:
Сумма: ${param.a + param.b}
Произведение: ${param.a * param.b}
</body>
</html>

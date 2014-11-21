<%--
  Комментарии
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.PrintWriter" %>
<html>
<head>
    <title></title>
</head>
<body>


<select>
    <%
        PrintWriter writer = new PrintWriter(out);
        String[] strings = new String[]{"aaa", "bbb", "ccc", "ddd"};

        for (int i = 0; i < strings.length; i++) {
            writer.printf("<option value='%d'>%s</option>", i, strings[i]);
        }
    %>
</select>
</body>
</html>

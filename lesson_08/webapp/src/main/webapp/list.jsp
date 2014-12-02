<%@ page import="webapp.Config" %>
<%@ page import="webapp.model.ContactType" %>
<%@ page import="webapp.model.Resume" %>
<%@ page import="webapp.web.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<header>Список всех резюме</header>
<section>
    <table>
        <tr>
            <td colspan="5" style="text-align: right"><a href="resume?action=create"><img src="img/add.png"> Добавить
                Резюме</a></td>
        </tr>
        <tr>
            <td>
                <table border="1" cellpadding="8" cellspacing="0">
                    <tr>
                        <th>Имя</th>
                        <th>Проживание</th>
                        <th>Email</th>
                        <th><%=HtmlUtil.EMPTY_TD%>
                        </th>
                        <th><%=HtmlUtil.EMPTY_TD%>
                        </th>
                    </tr>
                    <% for (Resume r : Config.getStorage().getAllSorted()) { %>
                    <tr>
                        <td>
                            <a href="resume?uuid=<%=r.getUuid()%>&action=view"><%=r.getFullName()%>
                            </a>
                        </td>
                        <td><%=HtmlUtil.mask(r.getLocation())%>
                        </td>
                        <td><%=r.getContact(ContactType.MAIL) == null ? HtmlUtil.EMPTY_TD : ContactType.MAIL.toHtml(r.getContact(ContactType.MAIL))%>
                        </td>
                        <td><a href="resume?uuid=<%=r.getUuid()%>&action=delete"><img src="img/delete.png"></a></td>
                        <td><a href="resume?uuid=<%=r.getUuid()%>&action=edit"><img src="img/pencil.png"></a></td>
                    </tr>
                    <% } %>

                </table>
            </td>
            <td>
            </td>
        </tr>
    </table>
</section>
</body>
</html>
<%@ page import="webapp.model.*" %>
<%@ page import="webapp.util.DateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Resume resume = (Resume) (request.getAttribute("resume"));
%>

<head>
    <link rel="stylesheet" href="css/style.css">
    <STYLE type="text/css">
        th, td {
            padding: 4px 10px 4px 0;
            vertical-align: top;
        }

        tr {
            border-bottom: 1px solid #DDDDDD;
        }

        table {
            margin-bottom: 1.4em;
        }

        table {
            border-collapse: collapse;
            border-spacing: 0;
        }
    </STYLE>
    <title>Резюме <%=resume.getFullName()%>
    </title>
</head>
<body>
<section>

    <h2>${resume.getFullName()}</h2>
    Проживание: ${resume.getLocation()}
    <p>
            <%
    for (ContactType type: resume.getContacts().keySet()){
        out.println(type.toHtml(resume.getContact(type))+"<br>");
    }
    %>

    <p>
    <table cellpadding="8">
        <% for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
            SectionType type = entry.getKey();
            switch (type) {
                case ACHIEVEMENT:
                case OBJECTIVE:
                case QUALIFICATIONS:
                    List<String> items = (List<String>) entry.getValue().getValues();
        %>
        <tr>
            <td><h3><%=type.getTitle()%>
            </h3></td>
            <td>
                <%
                    if (items.size() == 1) {
                        if (type == SectionType.OBJECTIVE) {
                            out.println("<b>" + items.get(0) + "</b>");
                        } else {
                            out.println(items.get(0));
                        }
                    } else {
                        out.println("<ul>");
                        for (String item : items) {
                            out.println("<li>" + item + "</li>");
                        }
                        out.println("</ul>");
                    }
                %>
            </td>
        </tr>
        <% break;
            case EXPERIENCE:
            case EDUCATION:
        %>
        <tr>
            <td><h3><%=type.getTitle()%>
            </h3></td>
        </tr>
        <%
            for (Organization org : (List<Organization>) entry.getValue().getValues()) {
        %>
        <tr>
            <td colspan="2"><br><a href="<%=org.getLink().getUrl()%>"><%=org.getLink().getName()%>
            </a></td>
        </tr>
        <tr>
                <%
                    for(Period item: org.getPeriods()){
        %>
        <tr>
            <td><%=DateUtil.format(item.getStartDate())%> - <%=DateUtil.format(item.getEndDate())%>
            </td>
            <td><%=item.getContent()%>
            </td>
        </tr>
        <%
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Type " + type + " is not implemented");
                }
            }
        %>
    </table>
    <button onclick="window.history.back()">ОК</button>
</section>
</body>
</html>
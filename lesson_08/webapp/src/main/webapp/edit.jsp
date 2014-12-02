<%@ page import="webapp.model.*" %>
<%@ page import="webapp.util.DateUtil" %>
<%@ page import="webapp.util.Util" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<%
    Resume resume = (Resume) (request.getAttribute("resume"));
%>
<head>
    <link rel="stylesheet" href="css/style.css">
    <title>Резюме ${resume.getFullName()}</title>
    <script language="JavaScript">
        function deleteSection(id) {
            var item = document.getElementById(id);
            item.parentNode.removeChild(item);
        }
    </script>
</head>
<body>
<header>Резюме ${resume.getFullName()}
</header>
<section>
    <form id="resume" method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.getUuid()}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="name" size=50 value="${resume.getFullName()}"></dd>
        </dl>
        <dl>
            <dt>Проживание:</dt>
            <dd><input type="text" name="location" size=50 value="${resume.getLocation()}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <%
            for (ContactType type : ContactType.values()) {
        %>
        <dl>
            <dt><%=type.getTitle()%>:</dt>
            <dd><input type="text" name="<%=type.name()%>" size=30 value="<%=Util.mask(resume.getContact(type))%>">
            </dd>
        </dl>
        <%
            }
        %>
        <h3>Разделы:</h3>
        <%
            int itemNum = 0;
            for (Map.Entry<SectionType, Section> e : resume.getSections().entrySet()) {
                SectionType type = e.getKey();
                Section s = e.getValue();
        %>
        <h4><%=type.getTitle()%>
        </h4>

        <%
            int count = 0;
            for (Object item : s.getValues()) {
                itemNum++;

                switch (type) {
                    case OBJECTIVE:
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
        %>
            <span id='s_<%=itemNum%>'>
                <dl>
                    <dd>
                        <%
                            if (type == SectionType.ACHIEVEMENT) {
                        %>
                        <textarea name="<%=type.name()%>" rows=5 cols=75><%=item%>
                        </textarea>
                        <%
                        } else {
                        %>
                        <input type='text' name='<%=type.name()%>' size=80 value='<%=item%>'>
                        <%
                            }
                            if (type != SectionType.OBJECTIVE) {
                        %>
                        <span class="small"><a href="#"
                                               onClick="deleteSection('s_<%=itemNum%>');return false;">Удалить</a></span>
                        <%
                            }
                        %>
                    </dd>
                    <br>
                </dl>
            </span>
        <%
                break;
            case EXPERIENCE:
            case EDUCATION:
                Organization org = (Organization) item;
        %>
        <div id='s_<%=itemNum%>' class="section_item">
            <dl>
                <dt>Название учереждения:</dt>
                <dd><input type="text" name='<%=type.name()%>' size=100 value="<%=org.getLink().getName()%>"></dd>
            </dl>
            <dl>
                <dt>Сайт учереждения:</dt>
                <dd><input type="text" name='<%=type.name()%>_orgUrl' size=100
                           value="<%=Util.mask(org.getLink().getUrl())%>">
                </dd>
            </dl>
            <%
                for (Period p : org.getPeriods()) {
                    itemNum++;
                    String pfx = type.name() + count;%>

            <div id='s_<%=itemNum%>' class="section_item">
                <dl>
                    <dt>Начальная дата:</dt>
                    <dd>
                        <select name="<%=pfx%>_startMonth">
                            <option value=-1 disabled></option>
                            <%
                                Date startDate = p.getStartDate();
                                int startMonth = DateUtil.getMonth(startDate);
                                for (int j = 0; j < DateUtil.MONTH.length - 1; j++) {
                                    out.println("<option value=" + j + (startMonth == j ? " selected>" : ">") + DateUtil.MONTH[j + 1] + "</option>");
                                }
                            %>
                        </select>
                        <input type="text" name="<%=pfx%>_startYear" size=10 value="<%=DateUtil.getYear(startDate)%>">
                    </dd>
                </dl>
                <dl>
                    <dt>Конечная дата:</dt>
                    <dd>
                        <select name="<%=pfx%>_endMonth">
                            <%
                                Date endDate = p.getEndDate();
                                int endMonth = DateUtil.getMonth(endDate);
                                for (int j = 0; j < DateUtil.MONTH.length - 1; j++) {
                                    out.println("<option value=" + j + (endMonth == j ? " selected>" : ">") + DateUtil.MONTH[j + 1] + "</option>");
                                }
                            %>
                        </select>
                        <input type="text" name="<%=pfx%>_endYear" size=10 value="<%=DateUtil.getYear(endDate)%>"></dd>
                </dl>
                <dl>
                    <dt>Позиция:</dt>
                    <dd><input type="text" name='<%=pfx%>_position' size=100 value="<%=Util.mask(p.getPosition())%>">
                </dl>
                <dl>
                    <dt>Содержание:</dt>
                    <dd><textarea name="<%=pfx%>_content" rows=5 cols=75><%=Util.mask(p.getContent())%>
                    </textarea></dd>
                </dl>
                <br>
                <span class="small"><a href="#"
                                       onClick="deleteSection('s_<%=itemNum%>');return false;">Удалить</a></span>
            </div>
            <%
                }
            %>
            <span class="small"><a href="#" onClick="deleteSection('s_<%=itemNum%>');return false;">Удалить</a></span>
        </div>
        <%
                            break;

                        default:
                            throw new IllegalArgumentException("Type " + type + " is not implemented");
                    }
                    count++;
                }
            }
        %>
        <br>
        <button type="submit"><%-- onclick="return validate()--%>Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>

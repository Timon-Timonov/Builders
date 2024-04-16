<%@ page import="it.academy.dto.AddressDto" %>
<%@ page import="it.academy.dto.ProjectDto" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page import="it.academy.dto.DeveloperDto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_developers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>The list of my developers</h2>
</div>
<%
    String actionName = MAIN_CONTRACTOR_SERVLET;
    String countName = DEVELOPER_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = DEVELOPER_PAGE_PARAM;

    String actionParameterToDoValue = WhatToDo.SHOW_DEVELOPERS.toString();
    int countOnPage = (Integer) session.getAttribute(DEVELOPER_COUNT_ON_PAGE_PARAM);
    int pageNumber = (Integer) session.getAttribute(DEVELOPER_PAGE_PARAM);
    ProjectStatus status = (ProjectStatus) session.getAttribute(PROJECT_STATUS_PARAM);
    List<DeveloperDto> developerDtoList = (List<DeveloperDto>) request.getAttribute(DEVELOPER_DTO_LIST_PARAM);
%>

<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/project_status_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
</div>
<br>

<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>DeveloperName</th>
            <th> |</th>
            <th>Address of developer</th>
            <th> |</th>
            <th>Debt by developer</th>
            <th> |</th>
            <th></th>
                  </tr>

        <%for (int i = 0; i < developerDtoList.size(); i++) {%>
        <%
            DeveloperDto developerDto = developerDtoList.get(i);
            String developerName = developerDto.getDeveloperName();
            String developerAddress = Optional.ofNullable(developerDto.getDeveloperAddress()).orElse(new AddressDto()).toString();
            String developerDebt=developerDto.getDeveloperDebt().toString();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=developerName%>
            </td>
            <td> |</td>
            <td><%=developerAddress%>
            </td>
            <td> |</td>
            <td><%=developerDebt%>
            </td>
            <td> |</td>
            <td>
                <form action="<%=GET_MY_PROJECTS_BY_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=developerDto.getId().toString()%>" name="<%=DEVELOPER_ID_PARAM%>">
                    <input type="hidden" value="<%=developerName%>" name="<%=DEVELOPER_NAME_PARAM%>">
                    <input type="hidden" value="<%=developerAddress%>" name="<%=DEVELOPER_ADDRESS_PARAM%>">
                    <input type="hidden" value="<%=developerDebt%>" name="<%=DEVELOPER_DEBT_PARAM%>">
                    <button class="btn btn-light" type="submit">Details</button>
                </form>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>
<br>
<div class="container text-center">
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

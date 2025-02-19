<%@ page import="it.academy.dto.AddressDto" %>
<%@ page import="it.academy.dto.ProjectDto" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="it.academy.servlet.utils.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_projects</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>The list of my projects</h2>
</div>
<%
    String actionName = MAIN_DEVELOPER_SERVLET;
    String countName = PROJECT_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROJECT_PAGE_PARAM;

    String actionParameterToDoValue = WhatToDo.SHOW_PROJECTS.toString();
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    ProjectStatus status = (ProjectStatus) session.getAttribute(PROJECT_STATUS_PARAM);
    List<ProjectDto> projectDtoList = (List<ProjectDto>) request.getAttribute(DTO_LIST_PARAM);
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
            <th>Name</th>
            <th> |</th>
            <th>Address of building object</th>
            <th> |</th>
            <th>Debt by project</th>
            <th> |</th>
            <th>Status of project</th>
            <th> |</th>
            <th>Total price of project</th>
            <th></th>
            <th></th>
        </tr>

        <%for (int i = 0; i < projectDtoList.size(); i++) {%>
        <%
            ProjectDto projectDto = projectDtoList.get(i);
            String projectName = projectDto.getProjectName();
            String projectAddress = Optional.ofNullable(projectDto.getProjectAddress()).orElse(new AddressDto()).toString();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=projectName%>
            </td>
            <td> |</td>
            <td><%=projectAddress%>
            </td>
            <td> |</td>
            <td><%=projectDto.getDebtByProject()%>
            </td>
            <td> |</td>
            <td><%=projectDto.getStatus().toString()%>
            </td>
            <td> |</td>
            <td><%=projectDto.getProjectPrice()%>
            </td>
            <td>
                <form action="<%=GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=projectDto.getId().toString()%>" name="<%=PROJECT_ID_PARAM%>">
                    <input type="hidden" value="<%=projectName%>" name="<%=PROJECT_NAME_PARAM%>">
                    <input type="hidden" value="<%=projectAddress%>" name="<%=PROJECT_ADDRESS_PARAM%>">
                    <button class="btn btn-light" type="submit">Details</button>
                </form>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>

<div class="container text-center">
    <form action="<%=CREATE_PROJECT_DEVELOPER_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">Create new project</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

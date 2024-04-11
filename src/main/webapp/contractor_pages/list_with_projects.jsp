<%@ page import="it.academy.dto.AddressDto" %>
<%@ page import="it.academy.dto.ProjectDto" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="it.academy.servlet.contractorServlets.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%@ page import="it.academy.util.Constants" %>
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
    <h2>The list of my projects</h2>
</div>
<%
    String actionName = Constants.MAIN_CONTRACTOR_SERVLET;
    String todoName = "todo";
    String countName = "project_count_on_page";
    String pageNumberParamName = "project_page";
    String projectStatus = "project_status";
    String projectDtoParamName = "project_dto_list";

    String actionParametrToDoValue = WhatToDo.SHOW_PROJECTS.toString();
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    ProjectStatus status = (ProjectStatus) session.getAttribute(projectStatus);
    List<ProjectDto> projectDtoList = (List<ProjectDto>) request.getAttribute(projectDtoParamName);
%>
<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
</div>
<br>
<div class="container text-center">
    <%@include file="/include_files/project_status_buttons_group.jsp" %>
</div>
<br>

<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th></th>
            <th>Name</th>
            <th></th>
            <th>Address of building object</th>
            <th></th>
            <th>Developer name</th>
            <th></th>
            <th>Debt by project</th>
            <th></th>
            <th>Status of project</th>
            <th></th>
            <th>Total price of project</th>
            <th></th>
            <th></th>
        </tr>

        <%for (int i = 0; i < projectDtoList.size(); i++) {%>
        <%ProjectDto projectDto = projectDtoList.get(i);%>

        <tr>
            <td><%=(i + 1)%>
            </td>
            <td></td>
            <td><%=projectDto.getProjectName()%>
            </td>
            <td></td>
            <td><%=Optional.ofNullable(projectDto.getProjectAddress()).orElse(new AddressDto()).toString()%>
            </td>
            <td></td>
            <td><%=projectDto.getDeveloperName()%>
            </td>
            <td></td>
            <td><%=projectDto.getDebtByProject()%>
            </td>
            <td></td>
            <td><%=projectDto.getStatus().toString()%>
            </td>
            <td></td>
            <td><%=projectDto.getProjectPrice()%>
            </td>
            <td>
                <form action="get_my_chapters_servlet" method="get">
                    <input type="hidden" value="<%=projectDto.getId().toString()%>" name="project_id">
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
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
    <br>
</div>
<%@include file="/include_files/go_to_main_button_file.jsp" %>
</body>
</html>

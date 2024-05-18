<%@ page import="it.academy.dto.AddressDto" %>
<%@ page import="it.academy.dto.DeveloperDto" %>
<%@ page import="it.academy.pojo.enums.Roles" %>
<%@ page import="it.academy.pojo.enums.UserStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ParameterNames.DEVELOPER_COUNT_ON_PAGE_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.DEVELOPER_PAGE_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page import="java.util.Optional" %>
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
    <h2>The list of all developers</h2>
</div>
<%
    String actionName = GET_ALL_DEVELOPERS_ADMINISTRATOR_SERVLET;
    String countName = DEVELOPER_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = DEVELOPER_PAGE_PARAM;

    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);
    UserStatus status = (UserStatus) session.getAttribute(USER_STATUS_PARAM);
    List<DeveloperDto> developerDtoList = (List<DeveloperDto>) request.getAttribute(DTO_LIST_PARAM);
%>

<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/user_status_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
</div>
<br>

<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Developer name</th>
            <th> |</th>
            <th>Address of developer</th>
            <th> |</th>
            <th></th>
            <th></th>
        </tr>

        <%for (int i = 0; i < developerDtoList.size(); i++) {%>
        <%
            DeveloperDto developerDto = developerDtoList.get(i);
            Long developerId = developerDto.getId();
            String developerName = developerDto.getDeveloperName();
            String developerAddress = Optional.ofNullable(developerDto.getDeveloperAddress()).orElse(new AddressDto()).toString();
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
            <td>
                <form action="<%=GET_PROJECTS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=developerId%>" name="<%=DEVELOPER_ID_PARAM%>">
                    <input type="hidden" value="<%=developerName%>" name="<%=DEVELOPER_NAME_PARAM%>">
                    <input type="hidden" value="<%=developerAddress%>" name="<%=DEVELOPER_ADDRESS_PARAM%>">
                    <button class="btn btn-light" type="submit">Show projects</button>
                </form>
            </td>
            <%if (UserStatus.ACTIVE.equals(status)) {%>
            <td>
                <form action="<%=CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=developerId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=UserStatus.CANCELED.toString()%>" name="<%=NEW_USER_STATUS_PARAM%>">
                    <input type="hidden" value="<%=Roles.DEVELOPER.toString()%>"
                           name="<%=ROLE_OF_UPDATING_USER_PARAM%>">
                    <button class="btn btn-light" type="submit">Cancel developer</button>
                </form>
            </td>
            <% } else if (UserStatus.CANCELED.equals(status)) {%>
            <td>
                <form action="<%=CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=developerId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=UserStatus.ACTIVE.toString()%>" name="<%=NEW_USER_STATUS_PARAM%>">
                    <input type="hidden" value="<%=Roles.DEVELOPER.toString()%>"
                           name="<%=ROLE_OF_UPDATING_USER_PARAM%>">
                    <button class="btn btn-light" type="submit">Return developer to active status</button>
                </form>
            </td>
            <%}%>
            <td>
                <form action="<%=DELETE_USER_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=developerId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=Roles.DEVELOPER.toString()%>"
                           name="<%=ROLE_OF_UPDATING_USER_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete developer</button>
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

<%@ page import="it.academy.dto.UserDto" %>
<%@ page import="it.academy.pojo.enums.UserStatus" %>
<%@ page import="it.academy.servlet.utils.WhatToDo" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_admins</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>

<div class="container text-center">
    <h2>The list of all administrators</h2>
</div>
<br>
<%
    String actionParameterToDoValue = WhatToDo.SHOW_ADMINISTRATORS.toString();
    List<UserDto> userDtoList = (List<UserDto>) request.getAttribute(DTO_LIST_PARAM);
%>

<br>


<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Email</th>
            <th> |</th>
            <th>Status</th>
            <th> |</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < userDtoList.size(); i++) {%>
        <%
            UserDto userDto = userDtoList.get(i);
            Long userId = userDto.getId();
            String userEmail = userDto.getEmail();
            UserStatus userStatus = userDto.getUserStatus();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=userEmail%>
            </td>
            <td> |</td>
            <td><%=userStatus.toString()%>
            </td>
            <td> |</td>

            <%if (UserStatus.ACTIVE.equals(userStatus)) {%>
            <td>
                <form action="<%=CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=userId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=UserStatus.CANCELED.toString()%>" name="<%=NEW_USER_STATUS_PARAM%>">
                    <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                    <button class="btn btn-light" type="submit">Cancel user</button>
                </form>
            </td>
            <% } else if (UserStatus.CANCELED.equals(userStatus)) {%>
            <td>
                <form action="<%=CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=userId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=UserStatus.ACTIVE.toString()%>" name="<%=NEW_USER_STATUS_PARAM%>">
                    <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                    <button class="btn btn-light" type="submit">Return user to active status</button>
                </form>
            </td>
            <%}%>
            <td>
                <form action="<%=DELETE_USER_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=userId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete user</button>
                </form>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
    <br>
    <br>
    <br>
    <form action="<%=CREATE_ADMIN_ADMINISTRATOR_SERVLET%>" method="post">

        Input email: <label>
        <input name="<%=EMAIL_PARAM%>" type="text">
    </label>
        Input password: <label>
        <input name="<%=PASSWORD_PARAM%>" type="text">
    </label>
        <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
        <br>
        <button class="btn btn-light" type="submit">Create new administrator</button>
    </form>

</div>
<br>
<br>
<div class="container text-center">
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

<%@ page import="it.academy.pojo.enums.Roles" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create_user_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>The create user form</h2>
</div>

<% Roles role = (Roles) session.getAttribute(ROLE_PARAM);
    String action;
    switch (role) {
        case CONTRACTOR:
            action = CREATE_CONTRACTOR_SERVLET;
            break;
        case DEVELOPER:
            action = CREATE_DEVELOPER_SERVLET;
            break;
        default:
            action = LOGOUT_SERVLET;
    }
%>
<div class="container text-center">
    <form action="<%=action%>" method="post">

        Input email: <label>
        <input name="<%=EMAIL_PARAM%>" type="text">
    </label><br/>
        <br>

        Input password: <label>
        <input name="<%=PASSWORD_PARAM%>" type="text">
    </label><br/>
        <br>

        Input name: <label>
        <input name="<%=NAME_PARAM%>" type="text">
    </label><br/>
        <br>

        Input city: <label>
        <input name="<%=CITY_PARAM%>" type="text">
    </label><br/>
        <br>

        Input street: <label>
        <input name="<%=STREET_PARAM%>" type="text">
    </label><br/>
        <br>

        Input building: <label>
        <input name="<%=BUILDING_PARAM%>" type="text">
    </label><br/>
        <br>
        <br>
        <button class="btn btn-primary" type="submit">Create new <%=role.toString().toLowerCase()%> account</button>
    </form>
    <%String ExitButtonName = "Cancel";%>
    <%@include file="/include_files/logout_button_file.jsp" %>
</div>
</body>
</html>

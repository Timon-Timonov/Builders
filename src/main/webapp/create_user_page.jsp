<%@ page import="it.academy.pojo.enums.Roles" %>
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

<% Roles role = (Roles) session.getAttribute("role");
    String action;
    switch (role) {
        case CONTRACTOR:
            action = "create_contractor_servlet";
            break;
        case DEVELOPER:
            action = "create_developer_servlet";
            break;
        default:
            action = "logout_servlet";
    }
%>
<div class="container text-center">
    <form action="<%=action%>" method="post">

        Input email: <label>
        <input name="email"  type="text">
    </label><br/>
        <br>

        Input password: <label>
        <input name="password"  type="text">
    </label><br/>
        <br>

        Input name: <label>
        <input name="name"  type="text">
    </label><br/>
        <br>

        Input city: <label>
        <input name="city"  type="text">
    </label><br/>
        <br>

        Input street: <label>
        <input name="street"  type="text">
    </label><br/>
        <br>

        Input building: <label>
        <input name="building"  type="text">
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

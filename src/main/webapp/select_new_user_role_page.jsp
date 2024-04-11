<%@ page import="it.academy.pojo.enums.Roles" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Select_user_role_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>You have to choose kind of account to create.</h2>
    <h2>It is impossible to change it later!</h2>
</div>
<div class="container text-center">

    <form action="create_user_servlet" method="post">
        <input type="hidden" value="<%=Roles.CONTRACTOR.toString()%>" name="role">
        <button class="btn btn-primary" type="submit">Create new contractor account</button>
    </form>

    <form action="create_user_servlet" method="post">
        <input type="hidden" value="<%=Roles.DEVELOPER.toString()%>" name="role">
        <button class="btn btn-primary" type="submit">Create new developer account</button>
    </form>
    <br>

    <%String ExitButtonName = "Back";%>
    <%@include file="/include_files/logout_button_file.jsp" %>
</div>
</body>
</html>

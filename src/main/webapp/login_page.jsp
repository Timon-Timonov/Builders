<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <form action="login_servlet" method="post">

        Input email: <label>
        <input name="email" value="<%=request.getParameter("email")!=null?request.getParameter("email"):""%>" type="text">
    </label><br/>
        <br>

        Input password: <label>
        <input name="password" value="<%=request.getParameter("password")!=null?request.getParameter("password"):""%>" type="text">
    </label><br/>
        <br>
        <br>

        <button class="btn btn-primary" type="submit">Login</button>
    </form>

    <%String ExitButtonName = "Back";%>
    <%@include file="/include_files/logout_button_file.jsp" %>
</div>
</body>
</html>

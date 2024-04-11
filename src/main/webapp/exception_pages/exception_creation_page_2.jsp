<%@ page import="it.academy.pojo.enums.Roles" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Exception_2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>
<body>
<div class="container text-center">
    <p>Ups... It is problem:</p>
    <%String message = (String) request.getAttribute("message");%>
    <p><%=message%>
    </p>

    <%String ExitButtonName = "Cancel";%>
    <%@include file="/include_files/logout_button_file.jsp" %>
</div>
</body>
</html>

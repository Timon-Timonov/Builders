<%@ page import="static it.academy.util.Constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Exception_3</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>
<body>
<div class="container text-center">
    <p>Ups... It is problem:</p>
    <%String message = (String) request.getAttribute(MESSAGE_PARAM);%>
    <p><%=message%>
    </p>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

<%@ page import="static it.academy.util.constants.ServletURLs.LOGIN_SERVLET" %>
<%@ page import="static it.academy.util.constants.ServletURLs.CREATE_USER_SERVLET" %>
<html>
<head>
    <title>Welcome_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>Hello! Please log in or create new account</h2>
    <form action="<%=LOGIN_SERVLET%>" method="get">
        <button class="btn btn-primary" type="submit">Login to account</button>
    </form>
    <form action="<%=CREATE_USER_SERVLET%>" method="get">
        <button class="btn btn-secondary" type="submit">Create new account</button>
    </form>
</div>
</body>
</html>

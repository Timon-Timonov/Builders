<%@ page import="it.academy.pojo.enums.Roles" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create_project_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>The create project form</h2>
</div>

<div class="container text-center">
    <form action="<%=CREATE_PROJECT_DEVELOPER_SERVLET%>" method="post">

        Input project name: <label>
        <input name="<%=PROJECT_NAME_PARAM%>" type="text">
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
        <button class="btn btn-primary" type="submit">Create new project</button>
    </form>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

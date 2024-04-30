<%@ page import="static it.academy.util.constants.ParameterNames.PROJECT_NAME_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.PROJECT_ADDRESS_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.CREATE_CHAPTER_DEVELOPER_SERVLET" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create_chapter_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">
    <h2>Create chapter form</h2>
    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Project status: <%=session.getAttribute(PROJECT_STATUS_PARAM)%>.</p>
</div>

<div class="container text-center">
    <form action="<%=CREATE_CHAPTER_DEVELOPER_SERVLET%>" method="post">

        Input chapter name: <label>
        <input name="<%=CHAPTER_NAME_PARAM%>" type="text">
    </label><br/>
        <br>

        Input chapter price: <label>
        <input name="<%=CHAPTER_PRICE_PARAM%>" type="text">
    </label><br/>
        <br>
        <br>

        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-primary" type="submit">Create new chapter</button>
    </form>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

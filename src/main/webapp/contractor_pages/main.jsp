<%@ page import="static it.academy.util.constants.ParameterNames.ROLE_PARAM" %>
<%@ page import="static it.academy.util.constants.Messages.ACCOUNT" %>
<%@ page import="static it.academy.util.constants.Messages.EXIT_FROM" %>
<%@ page import="static it.academy.util.constants.ServletURLs.GET_ALL_MY_DEVELOPERS_CONTRACTOR_SERVLET" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Main_contractor_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>
<body>


<div class="container text-center">
    <h2>The main contractor menu</h2>
</div>
<div class="container text-center">
    <table>
        <tr>
            <td>
                <form action="<%=GET_ALL_MY_PROJECTS_CONTRACTOR_SERVLET%>" method="get">
                    <button class="btn btn-secondary" type="submit">Show all my projects</button>
                </form>
            </td>
            <td><p></p></td>
            <td>
                <form action="<%=GET_ALL_MY_DEVELOPERS_CONTRACTOR_SERVLET%>" method="get">
                    <button class="btn btn-secondary" type="submit">Show all my developers</button>
                </form>
            </td>
            <td><p></p></td>
            <td>
                <form action="<%=GET_ALL_MY_PROPOSALS_CONTRACTOR_SERVLET%>" method="get">
                    <button class="btn btn-secondary" type="submit">Show all my proposals</button>
                </form>
            </td>
            <td><p></p></td>
            <td>
                <form action="<%=GET_ALL_CHAPTER_NAMES_CONTRACTOR_SERVLET%>" method="get">
                    <button class="btn btn-secondary" type="submit">Try to choose new project</button>
                </form>
            </td>
        </tr>
    </table>
    <br>
    <br>
    <%String ExitButtonName = EXIT_FROM + session.getAttribute(ROLE_PARAM).toString().toLowerCase() + ACCOUNT;%>
    <%@include file="/include_files/logout_button_file.jsp" %>
</div>
</body>
</html>

<%@ page import="it.academy.servlet.contractorServlets.WhatToDo" %>
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
    <h2>The main menu</h2>
</div>
<div class="container text-center">
    <table>
        <tr>
            <td>
                <form action="main_contractor_servlet" method="get">
                    <input type="hidden" value="<%=WhatToDo.SHOW_PROJECTS.toString()%>" name="todo">
                    <button class="btn btn-secondary" type="submit">Show all my projects</button>
                </form>
            </td>
            <td>
                <form action="main_contractor_servlet" method="get">
                    <input type="hidden" value="<%=WhatToDo.SHOW_DEVELOPERS.toString()%>" name="todo">
                    <button class="btn btn-secondary" type="submit">Show all my developers</button>
                </form>
            </td>
            <td>
                <form action="main_contractor_servlet" method="get">
                    <input type="hidden" value="<%=WhatToDo.SHOW_PROPOSALS.toString()%>" name="todo">
                    <button class="btn btn-secondary" type="submit">Show all my proposals</button>
                </form>
            </td>
            <td>
                <form action="main_contractor_servlet" method="get">
                    <input type="hidden" value="<%=WhatToDo.TRY_TO_CHOOSE_NEW_PROJECT.toString()%>" name="todo">
                    <button class="btn btn-secondary" type="submit">Try to choose new project</button>
                </form>
            </td>
        </tr>
    </table>
    <br>
    <br>
    <%String ExitButtonName = "Exit from " + session.getAttribute("role").toString().toLowerCase() + " account";%>
    <%@include file="/include_files/logout_button_file.jsp" %>
</div>
</body>
</html>

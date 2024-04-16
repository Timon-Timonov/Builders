<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_chapter_names</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<%
    String actionName = MAIN_CONTRACTOR_SERVLET;

    String actionParameterToDoValue = WhatToDo.TRY_TO_CHOOSE_NEW_PROJECT.toString();

    List<String> chapterNamesList = (List<String>) request.getAttribute(CHAPTER_NAMES_LIST_PARAM);
%>

<div class="container text-center">
    <h2>The list of all chapter names </h2>
</div>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < chapterNamesList.size(); i++) {%>
        <%
            String chapterName = chapterNamesList.get(i);
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=chapterName%>
            </td>
            <td>
                <form action="<%=GET_MY_PROPOSAL_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <button class="btn btn-light" type="submit">Try to choose chapters</button>
                </form>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>
<br>
<div class="container text-center">
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

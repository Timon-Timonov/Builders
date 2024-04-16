<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_chapters</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<%
    String actionName = GET_MY_CHAPTERS_SERVLET;
    String countName = CHAPTER_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CHAPTER_PAGE_PARAM;
    String chapterStatus = CHAPTER_STATUS_PARAM;

    String actionParameterToDoValue = null;

    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(CHAPTER_DTO_LIST_PARAM);
%>

<div class="container text-center">
    <h2>The list of my chapters from project </h2>
    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Developer name: <%=session.getAttribute(PROJECT_DEVELOPER_PARAM)%>.</p>
    <p>Developer status: <%=session.getAttribute(PROJECT_STATUS_PARAM)%>.</p>
</div>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th>Total chapter price</th>
            <th> |</th>
            <th>Debt by chapter</th>
            <th> |</th>
            <th>Status of chapter</th>
            <th></th>
            <th></th>
        </tr>
        <%for (int i = 0; i < chapterDtoList.size(); i++) {%>
        <%
            ChapterDto chapterDto = chapterDtoList.get(i);
            String chapterName = chapterDto.getChapterName();
            String chapterPrice = chapterDto.getChapterPrice().toString();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=chapterName%>
            </td>
            <td> |</td>
            <td><%=chapterPrice%>
            </td>
            <td > |</td>
            <td><%=chapterDto.getChapterDebt()%>
            </td>
            <td> |</td>
            <td><%=chapterDto.getChapterStatus().toString()%>
            </td>
            <td>
                <form action="<%=GET_MY_CALCULATION_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <input type="hidden" value="<%=chapterPrice%>" name="<%=CHAPTER_PRICE_PARAM%>">
                    <button class="btn btn-light" type="submit">Show this chapter</button>
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
    <% boolean showListByDeveloper = Optional.ofNullable((Boolean) session.getAttribute(SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM)).orElse(false);
        if (showListByDeveloper) {%>
    <form action="<%=GET_MY_PROJECTS_BY_DEVELOPER_SERVLET%>" method="get">
        <button class="btn btn-secondary" type="submit">Return to list with projects of this developer</button>
    </form>
    <br>
    <form action="<%=MAIN_CONTRACTOR_SERVLET%>" method="get">
        <input type="hidden" value="<%= WhatToDo.SHOW_DEVELOPERS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-secondary" type="submit">Return to list of developers</button>
    </form>
    <% } else {%>
    <form action="<%=MAIN_CONTRACTOR_SERVLET%>" method="get">
        <input type="hidden" value="<%=WhatToDo.SHOW_PROJECTS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-light" type="submit">Return to list with projects</button>
    </form>
    <%}%>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

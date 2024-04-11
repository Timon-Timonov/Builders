<%@ page import="it.academy.dto.AddressDto" %>
<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.pojo.enums.ChapterStatus" %>
<%@ page import="it.academy.servlet.contractorServlets.WhatToDo" %>
<%@ page import="it.academy.util.Constants" %>
<%@ page import="java.util.List" %>
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
    String actionName = Constants.GET_MY_CHAPTERS_SERVLET;
    String countName = "chapter_count_on_page";
    String pageNumberParamName = "chapter_page";
    String chapterStatus = "chapter_status";
    String chapterDtoParamName = "chapter_dto_list";

    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    ChapterStatus status = (ChapterStatus) session.getAttribute(chapterStatus);
    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(chapterDtoParamName);

    String todoName = "null";
    String actionParametrToDoValue = null;
%>

<div class="container text-center">
    <h2>The list of my chapters from project </h2>
    <p>Project name: <%=chapterDtoList.stream().findFirst().orElse(new ChapterDto()).getProjectName()%>.</p>
    <p>Project
        address: <%=chapterDtoList.stream().findFirst().orElse(new ChapterDto(new AddressDto())).getProjectAddress().toString()%>
        .</p>
    <p>Developer name: <%=chapterDtoList.stream().findFirst().orElse(new ChapterDto()).getDeveloperName()%>.</p>
</div>

<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
</div>
<br>
<div class="container text-center">
    <%@include file="/include_files/chapter_status_buttons_group.jsp" %>
</div>
<br>

<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th></th>
            <th>Chapter name</th>
            <th></th>
            <th>Total chapter price</th>
            <th></th>
            <th>Debt by chapter</th>
            <th></th>
            <th>Status of chapter</th>
            <th></th>
            <th></th>
        </tr>
        <%for (int i = 0; i < chapterDtoList.size(); i++) {%>
        <%ChapterDto chapterDto = chapterDtoList.get(i);%>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td><%=chapterDto.getChapterName()%>
            </td>
            <td><%=chapterDto.getChapterPrice()%>
            </td>
            <td><%=chapterDto.getChapterDebt()%>
            </td>
            <td><%=chapterDto.getChapterStatus().toString()%>
            </td>
            <td>
                <form action="get_my_chapters_servlet" method="post">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="chapter_id">
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
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
    <br>
</div>
<form action="main_contractor_servlet" method="get">
    <input type="hidden" value="<%=WhatToDo.SHOW_PROJECTS.toString()%>" name="todo">
    <button class="btn btn-light" type="submit">Return to list with projects</button>
</form>
<%@include file="/include_files/go_to_main_button_file.jsp" %>
</body>
</html>

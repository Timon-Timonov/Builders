<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
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
    String actionName = GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET;
    String countName = CHAPTER_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CHAPTER_PAGE_PARAM;

    String actionParameterToDoValue = null;
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    ProjectStatus status = (ProjectStatus) session.getAttribute(PROJECT_STATUS_PARAM);

    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(CHAPTER_DTO_LIST_PARAM);
%>

<div class="container text-center">
    <h2>The list of chapters with one contractor </h2>
    <p>Contractor name: <%=session.getAttribute(CHAPTER_CONTRACTOR_NAME_PARAM)%>.</p>
</div>

<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <%@include file="/include_files/project_status_buttons_group.jsp" %>
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
</div>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Project name</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th>Debt by chapter</th>
            <th> |</th>
            <th>Chapter price</th>
            <th> |</th>
            <th></th>
            <th></th>
        </tr>
        <%for (int i = 0; i < chapterDtoList.size(); i++) {%>
        <%
            ChapterDto chapterDto = chapterDtoList.get(i);

            String projectName = chapterDto.getProjectName();
            String chapterName = chapterDto.getChapterName();
            Integer chapterDebt = chapterDto.getChapterDebt();
            Integer chapterPrice = chapterDto.getChapterPrice();
            String contractorName = chapterDto.getContractorName();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=projectName%>
            </td>
            <td> |</td>
            <td><%=chapterName%>
            </td>
            <td> |</td>
            <td><%=chapterDebt%>
            </td>
            <td> |</td>
            <td><%=chapterPrice%>
            </td>
            <td> |</td>
            <td>
                <form action="<%=GET_MY_CALCULATION_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <input type="hidden" value="<%=chapterPrice%>" name="<%=CHAPTER_PRICE_PARAM%>">
                    <input type="hidden" value="<%=contractorName%>" name="<%=CHAPTER_CONTRACTOR_NAME_PARAM%>">
                    <input type="hidden" value="<%=chapterDto.getProjectAddress()%>" name="<%=PROJECT_ADDRESS_PARAM%>">
                    <input type="hidden" value="<%=chapterDto.getProjectName()%>" name="<%=PROJECT_NAME_PARAM%>">
                    <button class="btn btn-light" type="submit">Details</button>
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
    <br>
    <form action="<%=MAIN_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=WhatToDo.SHOW_CONTRACTORS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-light" type="submit">To list with contractors</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

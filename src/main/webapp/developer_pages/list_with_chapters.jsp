<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.pojo.enums.ChapterStatus" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="it.academy.servlet.developerServlets.ProjectToDo" %>
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
    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(CHAPTER_DTO_LIST_PARAM);
%>

<div class="container text-center">
    <h2>The list of chapters of my project </h2>
    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Project status: <%=session.getAttribute(PROJECT_STATUS_PARAM)%>.</p>
</div>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th>Contractor name</th>
            <th> |</th>
            <th>Debt by chapter</th>
            <th> |</th>
            <th>Status of chapter</th>
            <th></th>
            <th>Chapter price</th>
            <th></th>
            <th></th>
        </tr>
        <%for (int i = 0; i < chapterDtoList.size(); i++) {%>
        <%
            ChapterDto chapterDto = chapterDtoList.get(i);

            String chapterName = chapterDto.getChapterName();
            String contractorName = chapterDto.getContractorName();
            Integer chapterDebt = chapterDto.getChapterDebt();
            ChapterStatus chapterStatus = chapterDto.getChapterStatus();
            Integer chapterPrice = chapterDto.getChapterPrice();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=chapterName%>
            </td>
            <td> |</td>
            <td><%=contractorName%>
            </td>
            <td> |</td>
            <td><%=chapterDebt%>
            </td>
            <td> |</td>
            <td><%=chapterStatus.toString()%>
            </td>
            <td> |</td>
            <td><%=chapterPrice%>
            </td>
            <td> |</td>
            <td>
                <%if (ChapterStatus.OCCUPIED.equals(chapterStatus)) {%>
                <form action="<%=GET_MY_CALCULATION_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <input type="hidden" value="<%=chapterPrice%>" name="<%=CHAPTER_PRICE_PARAM%>">
                    <input type="hidden" value="<%=contractorName%>" name="<%=CHAPTER_CONTRACTOR_NAME_PARAM%>">
                    <button class="btn btn-light" type="submit">Details</button>
                </form>
                <%} else if (ChapterStatus.FREE.equals(chapterStatus)) {%>
                <form action="<%=GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <input type="hidden" value="<%=chapterPrice%>" name="<%=CHAPTER_PRICE_PARAM%>">
                    <button class="btn btn-light" type="submit">Show proposals</button>
                </form>
                <%}%>
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
    <form action="<%=CREATE_CHAPTER_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Create new chapter in this project</button>
    </form>
    <br>








    <%
        if (ProjectStatus.PREPARATION.equals(session.getAttribute(PROJECT_STATUS_PARAM))) {%>
    <form action="<%=                   %>" method="get">
        <input type="hidden" value="<%=ProjectToDo.START%>" name="<%=PROJECT_TODO_PARAM%>">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Start building process</button>
    </form>

    <% } else if (ProjectStatus.IN_PROCESS.equals(session.getAttribute(PROJECT_STATUS_PARAM))) {%>
    <form action="<%=                        %>" method="get">
        <input type="hidden" value="<%=ProjectToDo.FINISH%>" name="<%=PROJECT_TODO_PARAM%>">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Finish construction</button>
    </form>
    <%}%>
    <% if (!ProjectStatus.CANCELED.equals(session.getAttribute(PROJECT_STATUS_PARAM))) {%>
    <form action="<%=                     %>" method="get">
        <input type="hidden" value="<%=ProjectToDo.DROP%>" name="<%=PROJECT_TODO_PARAM%>">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Drop project</button>
    </form>
    <%}%>















    <form action="<%=MAIN_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=WhatToDo.SHOW_PROJECTS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-light" type="submit">Return to list with projects</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

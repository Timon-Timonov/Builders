<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.pojo.enums.ChapterStatus" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="static it.academy.util.constants.Constants.*" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.Messages.CONTRACTOR_NOT_SELECTED" %>
<%@ page import="static it.academy.util.constants.Messages.MINUS_STRING" %>
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
    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(DTO_LIST_PARAM);
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
            <td><%=contractorName == null ? CONTRACTOR_NOT_SELECTED : contractorName%>
            </td>
            <td> |</td>
            <td><%=chapterDebt == ZERO_INT_VALUE ? MINUS_STRING : chapterDebt.toString()%>
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
            <td>
                <%if (!ChapterStatus.CANCELED.equals(chapterStatus)) {%>
                <form action="<%=CANCEL_CHAPTER_STATUS_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Cancel chapter</button>
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
    <%
        ProjectStatus status = (ProjectStatus) session.getAttribute(PROJECT_STATUS_PARAM);
        if (ProjectStatus.PREPARATION.equals(status) ||
                    ProjectStatus.IN_PROCESS.equals(status)) {
    %>
    <br>
    <form action="<%=CREATE_CHAPTER_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Create new chapter in this project</button>
    </form>
    <br>
    <%}%>

    <%
        if (ProjectStatus.PREPARATION.equals(status)) {%>
    <form action="<%=PROJECT_STATUS_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=ProjectStatus.IN_PROCESS%>" name="<%=NEW_PROJECT_STATUS_PARAM%>">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Start building process</button>
    </form>

    <% } else if (ProjectStatus.IN_PROCESS.equals(status)) {%>
    <form action="<%=PROJECT_STATUS_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=ProjectStatus.COMPLETED%>" name="<%=NEW_PROJECT_STATUS_PARAM%>">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Finish construction</button>
    </form>
    <%}%>
    <% if (!ProjectStatus.CANCELED.equals(status) &&
            !ProjectStatus.COMPLETED.equals(status)) {%>
    <form action="<%=PROJECT_STATUS_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=ProjectStatus.CANCELED%>" name="<%=NEW_PROJECT_STATUS_PARAM%>">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Drop project</button>
    </form>
    <%}%>
    <form action="<%=GET_ALL_MY_PROJECTS_DEVELOPER_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with projects</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

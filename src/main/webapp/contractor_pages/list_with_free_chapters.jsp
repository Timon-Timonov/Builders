<%@ page import="it.academy.dto.AddressDto" %>
<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_free_chapters</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<%
    String actionName = GET_MY_PROPOSAL_SERVLET;
    String countName = CHAPTER_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CHAPTER_PAGE_PARAM;

    String actionParameterToDoValue = null;
    int countOnPage = (Integer) session.getAttribute(CHAPTER_COUNT_ON_PAGE_PARAM);
    int pageNumber = (Integer) session.getAttribute(CHAPTER_PAGE_PARAM);
    ProjectStatus status = (ProjectStatus) session.getAttribute(PROJECT_STATUS_PARAM);
    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(CHAPTER_DTO_LIST_PARAM);
%>
<div class="container text-center">
    <h2>The list of free <%=session.getAttribute(CHAPTER_NAME_PARAM).toString()%> chapters</h2>
</div>


<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>

    <p>Select the current status of projects to display chapters on page: </p>
    <table>
        <tr>
            <td>
                <form action="<%=GET_MY_PROPOSAL_SERVLET%>" method="get">
                    <input type="hidden" value="<%=ProjectStatus.PREPARATION.toString()%>"
                           name="<%=PROJECT_STATUS_PARAM%>">
                    <button class="<%=ProjectStatus.PREPARATION.equals(status)?"btn btn-success":"btn btn-light"%>"
                            type="submit">Status <%=ProjectStatus.PREPARATION.toString().toLowerCase()%>
                    </button>
                </form>
            </td>
            <td>
                <form action="<%=GET_MY_PROPOSAL_SERVLET%>" method="get">
                    <input type="hidden" value="<%=ProjectStatus.IN_PROCESS.toString()%>"
                           name="<%=PROJECT_STATUS_PARAM%>">
                    <button class="<%=ProjectStatus.IN_PROCESS.equals(status)?"btn btn-success":"btn btn-light"%>"
                            type="submit">Status <%=ProjectStatus.IN_PROCESS.toString().toLowerCase()%>
                </form>
            </td>
        </tr>
    </table>
    <br>
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
            <th>Address of building object</th>
            <th> |</th>
            <th>Developer name</th>
            <th> |</th>
            <th>Chapter price</th>
            <th> |</th>
            <th></th>
        </tr>

        <%
            for (int i = 0; i < chapterDtoList.size(); i++) {
                ChapterDto chapterDto = chapterDtoList.get(i);
                String projectName = chapterDto.getProjectName();
                String projectAddress = Optional.ofNullable(
                        chapterDto.getProjectAddress()).orElse(new AddressDto()).toString();
                String projectDeveloperName = chapterDto.getDeveloperName();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=projectName%>
            </td>
            <td> |</td>
            <td><%=projectAddress%>
            </td>
            <td> |</td>
            <td><%=projectDeveloperName%>
            </td>
            <td> |</td>
            <td><%=chapterDto.getChapterPrice()%>
            </td>
            <td> |</td>
            <td>
                <form action="<%=GET_MY_CHAPTERS_SERVLET%>" method="post">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Submit a proposal</button>
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

    <form action="<%=GET_MY_PROPOSAL_SERVLET%>" method="get">
        <button class="btn btn-secondary" type="submit">Return to list of free chapters</button>
    </form>

    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

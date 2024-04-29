<%@ page import="it.academy.dto.ChapterDto" %>
<%@ page import="it.academy.servlet.utils.WhatToDo" %>
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
    List<ChapterDto> chapterDtoList = (List<ChapterDto>) request.getAttribute(DTO_LIST_PARAM);
%>

<div class="container text-center">
    <h2>The list of chapters of contractor</h2>
</div>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th>Chapter contractor</th>
            <th> |</th>
            <th>Total chapter price</th>
            <th> |</th>
            <th>Status of chapter</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        <%for (int i = 0; i < chapterDtoList.size(); i++) {%>
        <%
            ChapterDto chapterDto = chapterDtoList.get(i);
            String chapterName = chapterDto.getChapterName();
            String chapterContractorName = chapterDto.getContractorName();
            String chapterPrice = chapterDto.getChapterPrice().toString();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=chapterName%>
            </td>
            <td> |</td>
            <td><%=chapterContractorName%>
            </td>
            <td> |</td>
            <td><%=chapterPrice%>
            </td>
            <td> |</td>
            <td><%=chapterDto.getChapterStatus().toString()%>
            </td>
            <td>
                <form action="<%=GET_CALCULATION_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <input type="hidden" value="<%=chapterPrice%>" name="<%=CHAPTER_PRICE_PARAM%>">
                    <button class="btn btn-light" type="submit">Show calculations</button>
                </form>
            </td>
            <td>
                <form action="<%=GET_PROPOSALS_FROM_CHAPTER_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <input type="hidden" value="<%=chapterName%>" name="<%=CHAPTER_NAME_PARAM%>">
                    <button class="btn btn-light" type="submit">Show proposals</button>
                </form>
            </td>
            <td>
                <form action="<%=DELETE_CHAPTER_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=chapterDto.getId().toString()%>" name="<%=CHAPTER_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete this chapter</button>
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
    <form action="<%=MAIN_ADMINISTRATOR_SERVLET%>" method="get">
        <input type="hidden" value="<%= WhatToDo.SHOW_CONTRACTORS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-secondary" type="submit">To list with contractors</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

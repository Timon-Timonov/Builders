<%@ page import="it.academy.dto.ProposalDto" %>
<%@ page import="it.academy.pojo.enums.ProposalStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ParameterNames.PROPOSAL_COUNT_ON_PAGE_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.PROPOSAL_PAGE_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_proposals</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<%

    String actionName = GET_PROPOSALS_FROM_CHAPTER_ADMINISTRATOR_SERVLET;
    String countName = PROPOSAL_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROPOSAL_PAGE_PARAM;

    String actionParameterToDoValue = null;
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    ProposalStatus status = (ProposalStatus) session.getAttribute(PROPOSAL_STATUS_PARAM);
    List<ProposalDto> proposalDtoList = (List<ProposalDto>) request.getAttribute(DTO_LIST_PARAM);

    String projectName = (String) session.getAttribute(PROJECT_NAME_PARAM);
    String chapterName = (String) session.getAttribute(CHAPTER_NAME_PARAM);
    int chapterPrice = (Integer) session.getAttribute(CHAPTER_PRICE_PARAM);
%>
<div class="container text-center">
    <h2>The list of proposals of chapter</h2>
    <p>Name of project: <%=projectName%>
    </p>
    <p>Name of chapter: <%=chapterName%>
    </p>
    <p>Price of chapter: <%=chapterPrice%>
    </p>
</div>


<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/proposal_status_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
</div>
<br>

<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Creation date</th>
            <th> |</th>
            <th>Contractor name</th>
            <th> |</th>
            <th></th>
            <th></th>
        </tr>
        <%
            for (int i = 0; i < proposalDtoList.size(); i++) {
                ProposalDto proposalDto = proposalDtoList.get(i);
                String proposalId = proposalDto.getId().toString();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getCreatedDate().toString()%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getContractorName()%>
            </td>
            <td></td>
            <td>
                <form action="<%=DELETE_PROPOSAL_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=proposalId%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete proposal</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
</div>
<br>
<br>
<div class="container text-center">
    <%if (session.getAttribute(PROJECT_ID_PARAM) != null) {%>
    <form action="<%=GET_CHAPTERS_FROM_PROJECT_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with chapters of project</button>
    </form>
    <% } %>

    <%if (session.getAttribute(CONTRACTOR_ID_PARAM) != null) {%>
    <form action="<%=GET_CHAPTERS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with chapters of contractor</button>
    </form>
    <br>
    <% } %>

    <%if (session.getAttribute(DEVELOPER_ID_PARAM) != null) {%>
    <form action="<%=GET_PROJECTS_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with project</button>
    </form>
    <br>
    <% } %>


    <%if (session.getAttribute(DEVELOPER_ID_PARAM) != null) {%>
    <form action="<%=GET_PROJECTS_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with projects of developer</button>
    </form>
    <% } %>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

<%@ page import="it.academy.dto.ProposalDto" %>
<%@ page import="it.academy.pojo.enums.ProposalStatus" %>
<%@ page import="java.util.List" %>
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

<div class="container text-center">
    <h2>The list of proposals of chapter of my project </h2>
    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Project status: <%=session.getAttribute(PROJECT_STATUS_PARAM)%>.</p>
    <p>Chapter name: <%=session.getAttribute(CHAPTER_NAME_PARAM)%>.</p>
    <p>Chapter price: <%=session.getAttribute(CHAPTER_PRICE_PARAM)%>.</p>
</div>
<br>
<%
    String actionName = GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET;
    String countName = PROPOSAL_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROPOSAL_PAGE_PARAM;
    boolean showProposalsFromChapter = true;

    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);
    ProposalStatus status = (ProposalStatus) session.getAttribute(PROPOSAL_STATUS_PARAM);
    List<ProposalDto> proposalDtoList = (List<ProposalDto>) request.getAttribute(DTO_LIST_PARAM);
%>
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
            <th>Contractor name</th>
            <th> |</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < proposalDtoList.size(); i++) {%>
        <%
            ProposalDto proposalDto = proposalDtoList.get(i);
            String contractorName = proposalDto.getContractorName();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=contractorName%>
            </td>
            <td> |</td>
            <%@include file="/include_files/change_proposal_status_button_groupe.jsp" %>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>
<br>
<div class="container text-center">
    <form action="<%=GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">Return to list with chapters of projects</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

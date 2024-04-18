<%@ page import="it.academy.dto.ProposalDto" %>
<%@ page import="it.academy.pojo.enums.ProposalStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.Constants.*" %>
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
    String todoName = TODO_PARAM;
    String countName = PROPOSAL_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROPOSAL_PAGE_PARAM;

    String actionParameterToDoValue = null;
    int countOnPage = (Integer) session.getAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM);
    int pageNumber = (Integer) session.getAttribute(PROPOSAL_PAGE_PARAM);
    ProposalStatus status = (ProposalStatus) session.getAttribute(PROPOSAL_STATUS_PARAM);
    List<ProposalDto> proposalDtoList = (List<ProposalDto>) request.getAttribute(PROPOSAL_DTO_LIST_PARAM);
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
            <td>
                <%if (ProposalStatus.APPROVED.equals(status) || ProposalStatus.CONSIDERATION.equals(status)) {%>
                <form action="<%=GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET%>" method="post">
                    <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <input type="hidden" value="<%=ProposalStatus.REJECTED.toString()%>"
                           name="<%=PROPOSAL_NEW_STATUS_PARAM%>">
                    <button class="btn btn-light" type="submit">Reject</button>
                </form>
                <%
                    }
                    if (ProposalStatus.CONSIDERATION.equals(status)) {
                %>
                <form action="<%=GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET%>" method="post">
                    <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <input type="hidden" value="<%=ProposalStatus.APPROVED.toString()%>"
                           name="<%=PROPOSAL_NEW_STATUS_PARAM%>">
                    <button class="btn btn-light" type="submit">Approve</button>
                </form>
                <%}%>
                <% if (ProposalStatus.REJECTED.equals(status)) {%>
                <form action="<%=GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET%>" method="post">
                    <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <input type="hidden" value="<%=ProposalStatus.CONSIDERATION.toString()%>"
                           name="<%=PROPOSAL_NEW_STATUS_PARAM%>">
                    <button class="btn btn-light" type="submit">Return to consideration status</button>
                </form>
                <% } %>
            </td>
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

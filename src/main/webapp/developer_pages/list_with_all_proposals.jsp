<%@ page import="it.academy.dto.ProposalDto" %>
<%@ page import="it.academy.pojo.enums.ProposalStatus" %>
<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page import="java.util.List" %>
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
    <h2>The list of my proposals of all my project </h2>
</div>
<br>
<%
    String actionName = MAIN_DEVELOPER_SERVLET;
    String todoName = TODO_PARAM;
    String countName = PROPOSAL_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROPOSAL_PAGE_PARAM;

    String actionParameterToDoValue = WhatToDo.SHOW_PROPOSALS.toString();
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
            <th>Project name</th>
            <th> |</th>
            <th>Rroject address</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th>Contractor name</th>
            <th> |</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < proposalDtoList.size(); i++) {%>
        <%
            ProposalDto proposalDto = proposalDtoList.get(i);

            String projectName = proposalDto.getProjectName();
            String projectAddress = proposalDto.getProjectAddress();
            String chapterName = proposalDto.getChapterName();
            String contractorName = proposalDto.getContractorName();
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
            <td><%=chapterName%>
            </td>
            <td> |</td>
            <td><%=contractorName%>
            </td>
            <td> |</td>
            <td>
                <%if (ProposalStatus.APPROVED.equals(status) || ProposalStatus.CONSIDERATION.equals(status)) {%>

                <form action="<%=actionName%>" method="post">
                    <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <input type="hidden" value="<%=ProposalStatus.REJECTED.toString()%>"
                           name="<%=PROPOSAL_NEW_STATUS_PARAM%>">
                    <button class="btn btn-light" type="submit">Reject</button>
                </form>
                <%
                    }
                    if (ProposalStatus.CONSIDERATION.equals(status)) {
                %>
                <form action="<%=actionName%>" method="post">
                    <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <input type="hidden" value="<%=ProposalStatus.APPROVED.toString()%>"
                           name="<%=PROPOSAL_NEW_STATUS_PARAM%>">
                    <button class="btn btn-light" type="submit">Approve</button>
                </form>
                <%}%>
                <% if (ProposalStatus.REJECTED.equals(status)) {%>

                <form action="<%=actionName%>" method="post">
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
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

<%@ page import="it.academy.dto.ProposalDto" %>
<%@ page import="it.academy.pojo.enums.ProposalStatus" %>
<%@ page import="it.academy.servlet.utils.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ServletURLs.MAIN_DEVELOPER_SERVLET" %>
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
    <h2>The list of my proposals of all my project </h2>
</div>
<br>
<%
    String actionName = MAIN_DEVELOPER_SERVLET;
    String countName = PROPOSAL_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROPOSAL_PAGE_PARAM;

    String actionParameterToDoValue = WhatToDo.SHOW_PROPOSALS.toString();
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
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

            <%@include file="/include_files/change_proposal_status_button_groupe.jsp" %>
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

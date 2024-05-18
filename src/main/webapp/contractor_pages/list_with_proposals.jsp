<%@ page import="it.academy.dto.ProposalDto" %>
<%@ page import="it.academy.pojo.enums.ProposalStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ParameterNames.PROPOSAL_COUNT_ON_PAGE_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.PROPOSAL_PAGE_PARAM" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET" %>
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

    String actionName = GET_ALL_MY_PROPOSALS_CONTRACTOR_SERVLET;
    String countName = PROPOSAL_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = PROPOSAL_PAGE_PARAM;

    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);
    ProposalStatus status = (ProposalStatus) session.getAttribute(PROPOSAL_STATUS_PARAM);
    List<ProposalDto> proposalDtoList = (List<ProposalDto>) request.getAttribute(DTO_LIST_PARAM);
%>
<div class="container text-center">
    <h2>The list of my proposals</h2>
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
            <th>Project name</th>
            <th> |</th>
            <th>Address of building object</th>
            <th> |</th>
            <th>Status of project</th>
            <th> |</th>
            <th>Developer name</th>
            <th> |</th>
            <th>Chapter name</th>
            <th> |</th>
            <th>Price of chapter</th>
            <th></th>
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
            <td><%=proposalDto.getProjectName()%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getProjectAddress()%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getProjectStatus()%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getDeveloperName()%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getChapterName()%>
            </td>
            <td> |</td>
            <td><%=proposalDto.getChapterPrice()%>
            </td>
            <td></td>
            <%
                switch (status) {
                    case APPROVED:
            %>
            <td>
                <form action="<%=CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=ProposalStatus.ACCEPTED_BY_CONTRACTOR.toString()%>"
                           name="<%=NEW_PROPOSAL_STATUS_PARAM%>">
                    <input type="hidden" value="<%=proposalId%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Start work</button>
                </form>
            </td>
            <%
                case CONSIDERATION:
            %>
            <td>
                <form action="<%=CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=ProposalStatus.CANCELED.toString()%>"
                           name="<%=NEW_PROPOSAL_STATUS_PARAM%>">
                    <input type="hidden" value="<%=proposalId%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Cancel proposal</button>
                </form>
            </td>
            <%
                    break;
                case CANCELED:
            %>
            <td>
                <form action="<%=CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=ProposalStatus.CONSIDERATION.toString()%>"
                           name="<%=NEW_PROPOSAL_STATUS_PARAM%>">
                    <input type="hidden" value="<%=proposalId%>" name="<%=PROPOSAL_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Return to consideration status</button>
                </form>
            </td>
            <%
                        break;
                }
            %>
        </tr>
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

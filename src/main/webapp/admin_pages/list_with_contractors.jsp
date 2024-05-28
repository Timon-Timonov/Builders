<%@ page import="it.academy.dto.ContractorDto" %>
<%@ page import="it.academy.pojo.enums.Roles" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page import="it.academy.pojo.enums.UserStatus" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_contractors</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>

<div class="container text-center">
    <h2>The list of all contractors</h2>
</div>
<br>
<%
    String actionName = GET_ALL_CONTRACTORS_ADMINISTRATOR_SERVLET;
    String countName = CONTRACTOR_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CONTRACTOR_PAGE_PARAM;

    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);
    UserStatus status = (UserStatus) session.getAttribute(USER_STATUS_PARAM);
    List<ContractorDto> contractorDtoList = (List<ContractorDto>) request.getAttribute(DTO_LIST_PARAM);
%>
<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/user_status_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
</div>
<br>
<%@include file="/include_files/search_line.jsp" %>
>

<br>

<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Contractor name</th>
            <th> |</th>
            <th>Address of contractor</th>
            <th> |</th>
            <th></th>
            <th></th>
        </tr>
        <%for (int i = 0; i < contractorDtoList.size(); i++) {%>
        <%
            ContractorDto contractorDto = contractorDtoList.get(i);
            Long contractorId = contractorDto.getId();
            String contractorName = contractorDto.getContractorName();
            String contractorAddress = contractorDto.getContractorAddress();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=contractorName%>
            </td>
            <td> |</td>
            <td><%=contractorAddress%>
            </td>
            <td> |</td>
            <td>
                <form action="<%=GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=contractorId%>" name="<%=CONTRACTOR_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Show proposals</button>
                </form>
            </td>
            <td>
                <form action="<%=GET_CHAPTERS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=contractorId%>" name="<%=CONTRACTOR_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Show chapters</button>
                </form>
            </td>


            <%if (UserStatus.ACTIVE.equals(status)) {%>
            <td>
                <form action="<%=CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=contractorId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=UserStatus.CANCELED.toString()%>" name="<%=NEW_USER_STATUS_PARAM%>">
                    <input type="hidden" value="<%=Roles.CONTRACTOR.toString()%>"
                           name="<%=ROLE_OF_UPDATING_USER_PARAM%>">
                    <button class="btn btn-light" type="submit">Cancel contractor</button>
                </form>
            </td>
            <% } else if (UserStatus.CANCELED.equals(status)) {%>
            <td>
                <form action="<%=CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=contractorId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=UserStatus.ACTIVE.toString()%>" name="<%=NEW_USER_STATUS_PARAM%>">
                    <input type="hidden" value="<%=Roles.CONTRACTOR.toString()%>"
                           name="<%=ROLE_OF_UPDATING_USER_PARAM%>">
                    <button class="btn btn-light" type="submit">Return contractor to active status</button>
                </form>
            </td>
            <%}%>
            <td>
                <form action="<%=DELETE_USER_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=contractorId%>" name="<%=USER_ID_PARAM%>">
                    <input type="hidden" value="<%=Roles.CONTRACTOR.toString()%>"
                           name="<%=ROLE_OF_UPDATING_USER_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete contractor</button>
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
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

<%@ page import="it.academy.dto.ContractorDto" %>
<%@ page import="it.academy.pojo.enums.ProjectStatus" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
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
    <h2>The list of all my contractors</h2>
</div>
<br>
<%
    String actionName = GET_ALL_MY_CONTRACTORS_DEVELOPER_SERVLET;
    String countName = CONTRACTOR_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CONTRACTOR_PAGE_PARAM;

    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);
    ProjectStatus status = (ProjectStatus) session.getAttribute(PROJECT_STATUS_PARAM);
    List<ContractorDto> contractorDtoList = (List<ContractorDto>) request.getAttribute(DTO_LIST_PARAM);
%>
<div class="container text-center">

    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/project_status_buttons_group.jsp" %>
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
            <th>Name</th>
            <th> |</th>
            <th>Address of contractor</th>
            <th> |</th>
            <th>Contractor debt</th>
            <th> |</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < contractorDtoList.size(); i++) {%>
        <%
            ContractorDto contractorDto = contractorDtoList.get(i);
            String contractorName = contractorDto.getContractorName();
            String contractorAddress = Optional.ofNullable(contractorDto.getContractorAddress()).orElse(BLANK_STRING);
            Integer contractorDebt = contractorDto.getContractorDebt();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=contractorName%>
            </td>
            <td> |</td>
            <td><%=contractorAddress%>
            <td> |</td>
            <td><%=contractorDebt%>
            </td>
            <td> |</td>
            <td>
                <form action="<%=GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=contractorDto.getId().toString()%>" name="<%=CONTRACTOR_ID_PARAM%>">
                    <input type="hidden" value="<%=contractorName%>" name="<%=CHAPTER_CONTRACTOR_NAME_PARAM%>">
                    <button class="btn btn-light" type="submit">Details</button>
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

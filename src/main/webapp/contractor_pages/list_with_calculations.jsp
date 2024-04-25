<%@ page import="static it.academy.util.constants.ServletURLs.CREATE_CALCULATION_CONTRACTOR_SERVLET" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="static it.academy.util.constants.ServletURLs.GET_MY_CALCULATION_CONTRACTOR_SERVLET" %>
<%@ page import="it.academy.dto.CalculationDto" %>
<%@ page import="it.academy.servlet.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_calculations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>

<%
    String actionName = GET_MY_CALCULATION_CONTRACTOR_SERVLET;
    String countName = CALCULATION_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CALCULATION_PAGE_PARAM;
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);

    String actionParameterToDoValue = null;

    List<CalculationDto> calculationDtoList = (List<CalculationDto>) request.getAttribute(CALCULATION_DTO_LIST_PARAM);
%>
<div class="container text-center">
    <h2>The list of my calculations from chapter </h2>

    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Developer name: <%=session.getAttribute(PROJECT_DEVELOPER_PARAM)%>.</p>
    <p>Chapter name: <%=session.getAttribute(CHAPTER_NAME_PARAM)%>.</p>
    <p>Chapter price: <%=session.getAttribute(CHAPTER_PRICE_PARAM)%>.</p>
</div>

<div class="container text-center">
    <%@include file="/include_files/count_on_page_buttons_group.jsp" %>
    <br>
    <%@include file="/include_files/pagination_buttons_group.jsp" %>
</div>
<br>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Year</th>
            <th> |</th>
            <th>Mounth</th>
            <th> |</th>
            <th>Advance</th>
            <th> |</th>
            <th>For work</th>
            <th> |</th>
            <th>Debt</th>
            <th> |</th>
            <th>PriceFact</th>
            <th> |</th>
            <th>PricePlan</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < calculationDtoList.size(); i++) {%>
        <%
            CalculationDto calculationDto = calculationDtoList.get(i);
            String workPriceFact = calculationDto.getWorkPriceFact() == null ?
                    ""
                    : calculationDto.getWorkPriceFact().toString();
        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getYYYY()%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getMM()%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getSumAdvance()%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getSumForWork()%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getCalculationDebt()%>
            </td>
            <td> |</td>
            <td><%=workPriceFact%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getWorkPricePlan()%>
            </td>
            <td>
                <form action="<%=GET_MY_CALCULATION_CONTRACTOR_SERVLET%>" method="post">
                    <input type="hidden" value="<%=calculationDto.getId().toString()%>"
                           name="<%=CALCULATION_ID_PARAM%>">
                    New Value of work price fact: <label>
                    <input type="text" value="<%=workPriceFact%>" name="<%=WORK_PRICE_FACT_PARAM%>">
                    <button class="btn btn-light" type="submit">Edit work price fact</button>
                </label>
                </form>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>
<div class="container text-center">
    <form action="<%=CREATE_CALCULATION_CONTRACTOR_SERVLET%>" method="get">
        <input type="hidden" value="<%=session.getAttribute(CHAPTER_ID_PARAM)%>" name="<%=CHAPTER_ID_PARAM%>">
        <button class="btn btn-light" type="submit">Create new calculation</button>
    </form>
    <br>
    <form action="<%=GET_MY_CHAPTERS_CONTRACTOR_SERVLET%>" method="get">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-light" type="submit">Return to list with chapters of project</button>
    </form>
    <br>
    <form action="<%=MAIN_CONTRACTOR_SERVLET%>" method="get">
        <input type="hidden" value="<%=WhatToDo.SHOW_PROJECTS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-light" type="submit">Return to list with projects</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

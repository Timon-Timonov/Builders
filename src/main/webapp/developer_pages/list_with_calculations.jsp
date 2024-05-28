<%@ page import="it.academy.dto.CalculationDto" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
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
    String actionName = GET_MY_CALCULATION_DEVELOPER_SERVLET;
    String countName = CALCULATION_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CALCULATION_PAGE_PARAM;
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);

    List<CalculationDto> calculationDtoList = (List<CalculationDto>) request.getAttribute(DTO_LIST_PARAM);

    Long projectId = (Long) session.getAttribute(PROJECT_ID_PARAM);
%>
<div class="container text-center">
    <h2>The list of my calculations from chapter </h2>

    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Chapter name: <%=session.getAttribute(CHAPTER_NAME_PARAM)%>.</p>
    <p>Chapter price: <%=session.getAttribute(CHAPTER_PRICE_PARAM)%>.</p>
    <p>Contractor name: <%=session.getAttribute(CHAPTER_CONTRACTOR_NAME_PARAM)%>.</p>
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
            <th>|</th>
            <th></th>
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
            <td><%=calculationDto.getYyyy()%>
            </td>
            <td> |</td>
            <td><%=calculationDto.getMm()%>
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
                <%
                    if (calculationDto.getSumAdvance() == ZERO_INT_VALUE &&
                            calculationDto.getSumForWork() == ZERO_INT_VALUE &&
                            calculationDto.getCalculationDebt() == ZERO_INT_VALUE) {
                %>
                <form action="<%=PAY_MONEY_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=calculationDto.getId().toString()%>"
                           name="<%=CALCULATION_ID_PARAM%>">
                    <input type="hidden" value="<%=calculationDto.getCalculationDebt()%>"
                           name="<%=CALCULATION_DEBT_PARAM%>">
                    Sum advance: <label>
                    <input type="text" name="<%=SUM_ADVANCE_PARAM%>">
                    <button class="btn btn-light" type="submit">Pay advance</button>
                </label>
                </form>
                <%}%>
            </td>
            <td>
                <%if (calculationDto.getCalculationDebt() > ZERO_INT_VALUE) {%>
                <form action="<%=PAY_MONEY_DEVELOPER_SERVLET%>" method="get">
                    <input type="hidden" value="<%=calculationDto.getId().toString()%>"
                           name="<%=CALCULATION_ID_PARAM%>">
                    <input type="hidden" value="<%=calculationDto.getCalculationDebt()%>"
                           name="<%=CALCULATION_DEBT_PARAM%>">
                    Sum for work: <label>
                    <input type="text" value="<%=calculationDto.getCalculationDebt()%>" name="<%=SUM_FOR_WORK_PARAM%>">
                    <button class="btn btn-light" type="submit">Pay for work</button>
                </label>
                </form>
                <%}%>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>
<div class="container text-center">

    <%if (projectId != null) {%>
    <form action="<%=GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=projectId%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-light" type="submit">To list with chapters of project</button>
    </form>
    <br>
    <%} else if (session.getAttribute(CONTRACTOR_ID_PARAM) != null) {%>
    <form action="<%=GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with chapters of contractor</button>
    </form>
    <br>
    <%}%>
    <form action="<%=GET_ALL_MY_PROJECTS_DEVELOPER_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with projects</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

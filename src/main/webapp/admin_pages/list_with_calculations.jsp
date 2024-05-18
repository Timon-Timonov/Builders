<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="it.academy.dto.CalculationDto" %>
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
    String actionName = GET_CALCULATION_ADMINISTRATOR_SERVLET;
    String countName = CALCULATION_COUNT_ON_PAGE_PARAM;
    String pageNumberParamName = CALCULATION_PAGE_PARAM;
    int countOnPage = (Integer) session.getAttribute(countName);
    int pageNumber = (Integer) session.getAttribute(pageNumberParamName);
    int lastPageNumber = (Integer) session.getAttribute(LAST_PAGE_NUMBER_PARAM);

    List<CalculationDto> calculationDtoList = (List<CalculationDto>) request.getAttribute(DTO_LIST_PARAM);
%>
<div class="container text-center">
    <h2>The list of calculations from chapter </h2>

    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
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
                <form action="<%=GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET%>" method="post">
                    <input type="hidden" value="<%=calculationDto.getId().toString()%>"
                           name="<%=CALCULATION_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Show money transfers</button>
                </form>
            </td>
            <td>
                <form action="<%=DELETE_CALCULATION_ADMINISTRATOR_SERVLET%>" method="post">
                    <input type="hidden" value="<%=calculationDto.getId().toString()%>"
                           name="<%=CALCULATION_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete calculation</button>
                </form>
            </td>
        </tr>
        <tr></tr>
        <% } %>
    </table>
</div>
<br>
<div class="container text-center">
    <br>

    <%if (session.getAttribute(PROJECT_ID_PARAM) != null) {%>
    <form action="<%=GET_CHAPTERS_FROM_PROJECT_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with chapters of project</button>
    </form>
    <br>
    <% } %>

    <%if (session.getAttribute(DEVELOPER_ID_PARAM) != null) {%>
    <form action="<%=GET_PROJECTS_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with project</button>
    </form>
    <br>
    <% } %>

    <form action="<%=GET_ALL_DEVELOPERS_ADMINISTRATOR_SERVLET%>" method="get">
        <button class="btn btn-light" type="submit">To list with developers</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

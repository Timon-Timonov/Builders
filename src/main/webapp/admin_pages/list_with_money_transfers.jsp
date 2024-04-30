<%@ page import="static it.academy.util.constants.ParameterNames.*" %>
<%@ page import="it.academy.dto.MoneyTransferDto" %>
<%@ page import="it.academy.servlet.utils.WhatToDo" %>
<%@ page import="java.util.List" %>
<%@ page import="static it.academy.util.constants.ServletURLs.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List_of_money_transfers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>

<%
    String actionName = GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET;


    List<MoneyTransferDto> moneyTransferDtoList = (List<MoneyTransferDto>) request.getAttribute(DTO_LIST_PARAM);
%>
<div class="container text-center">
    <h2>The list of money transfers from calculation </h2>

    <p>Project name: <%=session.getAttribute(PROJECT_NAME_PARAM)%>.</p>
    <p>Project address: <%=session.getAttribute(PROJECT_ADDRESS_PARAM)%>.</p>
    <p>Chapter name: <%=session.getAttribute(CHAPTER_NAME_PARAM)%>.</p>
    <p>Chapter price: <%=session.getAttribute(CHAPTER_PRICE_PARAM)%>.</p>
</div>

<br>
<br>
<div class="container text-center">
    <table>
        <tr>
            <th>No</th>
            <th> |</th>
            <th>Date of transfer</th>
            <th> |</th>
            <th>Sum</th>
            <th> |</th>
            <th>Payment type/th>
            <th> |</th>
            <th></th>
        </tr>
        <%for (int i = 0; i < moneyTransferDtoList.size(); i++) {%>
        <%
            MoneyTransferDto moneyTransferDto = moneyTransferDtoList.get(i);

        %>
        <tr>
            <td><%=(i + 1)%>
            </td>
            <td> |</td>
            <td><%=moneyTransferDto.getCalculationDate()%>
            </td>
            <td> |</td>
            <td><%=moneyTransferDto.getSum()%>
            </td>
            <td> |</td>
            <td><%=moneyTransferDto.getPaymentType().toString()%>
            </td>
            <td> |</td>
            <td>
                <form action="<%=DELETE_MONEY_TRANSFER_ADMINISTRATOR_SERVLET%>" method="get">
                    <input type="hidden" value="<%=moneyTransferDto.getId().toString()%>"
                           name="<%=MONEY_TRANSFER_ID_PARAM%>">
                    <button class="btn btn-light" type="submit">Delete money transfer</button>
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
    <% if (session.getAttribute(PROJECT_ID_PARAM) != null) { %>
    <form action="<%=GET_CHAPTERS_FROM_PROJECT_ADMINISTRATOR_SERVLET%>" method="get">
        <input type="hidden" value="<%=session.getAttribute(PROJECT_ID_PARAM)%>" name="<%=PROJECT_ID_PARAM%>">
        <button class="btn btn-light" type="submit">Return to list with chapters of project</button>
    </form>
    <br>
    <% } %>
    <form action="<%=MAIN_ADMINISTRATOR_SERVLET%>" method="get">
        <input type="hidden" value="<%=WhatToDo.SHOW_PROJECTS.toString()%>" name="<%=TODO_PARAM%>">
        <button class="btn btn-light" type="submit">Return to list with projects</button>
    </form>
    <br>
    <%@include file="/include_files/go_to_main_button_file.jsp" %>
</div>
</body>
</html>

<table>
    <tr>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=Constants.COUNT_ON_PAGE_3%>" name="<%=countName%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=countOnPage==Constants.COUNT_ON_PAGE_3?"btn btn-success":"btn btn-light"%>" type="submit"> show <%=Constants.COUNT_ON_PAGE_3%></button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=Constants.DEFAULT_COUNT_ON_PAGE_5%>" name="<%=countName%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=countOnPage==Constants.DEFAULT_COUNT_ON_PAGE_5?"btn btn-success":"btn btn-light"%>" type="submit"> show <%=Constants.DEFAULT_COUNT_ON_PAGE_5%></button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=Constants.COUNT_ON_PAGE_10%>" name="<%=countName%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=countOnPage==Constants.COUNT_ON_PAGE_10?"btn btn-success":"btn btn-light"%>" type="submit">show <%=Constants.COUNT_ON_PAGE_10%>
                </button>
            </form>
        </td>
    </tr>
</table>
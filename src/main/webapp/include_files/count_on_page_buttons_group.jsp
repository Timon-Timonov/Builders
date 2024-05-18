<%@ page import="static it.academy.util.constants.Constants.COUNT_ON_PAGE_3" %>
<%@ page import="static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER" %>
<%@ page import="static it.academy.util.constants.Constants.*" %>
<table>
    <tr>
        <td>
            <p>Select the number of elements on page to display: </p>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=COUNT_ON_PAGE_3%>" name="<%=countName%>">
                <input type="hidden" value="<%=FIRST_PAGE_NUMBER%>" name="<%=pageNumberParamName%>">
                <button class="<%=countOnPage==COUNT_ON_PAGE_3?"btn btn-success":"btn btn-light"%>" type="submit">
                    show <%=COUNT_ON_PAGE_3%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=DEFAULT_COUNT_ON_PAGE_5%>" name="<%=countName%>">
                <input type="hidden" value="<%=FIRST_PAGE_NUMBER%>" name="<%=pageNumberParamName%>">
                <button class="<%=countOnPage==DEFAULT_COUNT_ON_PAGE_5?"btn btn-success":"btn btn-light"%>"
                        type="submit"> show <%=DEFAULT_COUNT_ON_PAGE_5%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=COUNT_ON_PAGE_10%>" name="<%=countName%>">
                <input type="hidden" value="<%=FIRST_PAGE_NUMBER%>" name="<%=pageNumberParamName%>">
                <button class="<%=countOnPage==COUNT_ON_PAGE_10?"btn btn-success":"btn btn-light"%>" type="submit">
                    show <%=COUNT_ON_PAGE_10%>
                </button>
            </form>
        </td>
    </tr>
</table>
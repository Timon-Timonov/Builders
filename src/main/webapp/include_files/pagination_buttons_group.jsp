<table>
    <tr>
        <td>
            <p>Select the number of page to display: </p>
        </td>
        <td>
            <%if (lastPageNumber != FIRST_PAGE_NUMBER) {%>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=FIRST_PAGE_NUMBER%>" name="<%=pageNumberParamName%>">
                <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                <button class="btn btn-light" type="submit">First</button>
            </form>
            <%}%>
        </td>
        <td>
            <%if (lastPageNumber != FIRST_PAGE_NUMBER) {%>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=pageNumber-1%>" name="<%=pageNumberParamName%>">
                <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                <button class="btn btn-outline-secondary" type="submit">Previous</button>
            </form>
            <%}%>
        </td>
        <td>
            <p><%=pageNumber%>
            </p>
        </td>
        <td>
            <%if (lastPageNumber != FIRST_PAGE_NUMBER) {%>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=pageNumber+1%>" name="<%=pageNumberParamName%>">
                <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                <button class="btn btn-outline-primary" type="submit">Next</button>
            </form>
            <%}%>
        </td>
        <td>
            <%if (lastPageNumber != FIRST_PAGE_NUMBER) {%>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=DEFAULT_LAST_PAGE_NUMBER%>" name="<%=pageNumberParamName%>">
                <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                <button class="btn btn-light" type="submit">Last:<%=lastPageNumber%>
                </button>
            </form>
            <%}%>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=Constants.FIRST_PAGE_NUMBER%>" name="<%=pageNumber%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="btn btn-light" type="submit">First</button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=pageNumber-1%>" name="<%=pageNumber%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="btn btn-outline-secondary" type="submit">Previous</button>
            </form>
        </td>
        <td>
            <p><%=pageNumber%>
            </p>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=pageNumber+1%>" name="<%=pageNumber%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="btn btn-outline-primary" type="submit">Next</button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=Constants.DEFAULT_LAST_PAGE_NUMBER%>" name="<%=pageNumber%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="btn btn-light" type="submit">Last</button>
            </form>
        </td>
    </tr>
</table>
<table>
    <%UserStatus active = UserStatus.ACTIVE;%>
    <%UserStatus canceled = UserStatus.CANCELED;%>

    <tr>
        <td><p>Select the current status of projects to display on page: </p></td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=active.toString()%>" name="<%=USER_STATUS_PARAM%>">
                <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                <button class="<%=active.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=active.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=canceled.toString()%>" name="<%=USER_STATUS_PARAM%>">
                <input type="hidden" value="<%=actionParameterToDoValue%>" name="<%=TODO_PARAM%>">
                <button class="<%=canceled.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=canceled.toString().toLowerCase()%>
            </form>
        </td>
    </tr>
</table>
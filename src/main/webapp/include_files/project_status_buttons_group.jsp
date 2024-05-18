<table>
    <%ProjectStatus preparation = ProjectStatus.PREPARATION;%>
    <%ProjectStatus inProcess = ProjectStatus.IN_PROCESS;%>
    <%ProjectStatus completed = ProjectStatus.COMPLETED;%>
    <%ProjectStatus canceled = ProjectStatus.CANCELED;%>
    <tr>
        <td><p>Select the current status of projects to display on page: </p></td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=preparation.toString()%>" name="<%=PROJECT_STATUS_PARAM%>">
                <button class="<%=preparation.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=preparation.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=inProcess.toString()%>" name="<%=PROJECT_STATUS_PARAM%>">
                <button class="<%=inProcess.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=inProcess.toString().toLowerCase()%>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=completed.toString()%>" name="<%=PROJECT_STATUS_PARAM%>">
                <button class="<%=completed.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=completed.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=canceled.toString()%>" name="<%=PROJECT_STATUS_PARAM%>">
                <button class="<%=canceled.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=canceled.toString().toLowerCase()%>
                </button>
            </form>
        </td>
    </tr>
</table>
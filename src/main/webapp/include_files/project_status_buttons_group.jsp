<table>
    <tr>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=ProjectStatus.PREPARATION.toString()%>" name="<%="project_status"%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=ProjectStatus.PREPARATION.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=ProjectStatus.PREPARATION.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=ProjectStatus.IN_PROCESS.toString()%>" name="<%="project_status"%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=ProjectStatus.IN_PROCESS.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=ProjectStatus.IN_PROCESS.toString().toLowerCase()%>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=ProjectStatus.COMPLETED.toString()%>" name="<%="project_status"%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=ProjectStatus.COMPLETED.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=ProjectStatus.COMPLETED.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=ProjectStatus.CANCELED.toString()%>" name="<%="project_status"%>">
                <input type="hidden" value="<%=actionParametrToDoValue%>" name="<%=todoName%>">
                <button class="<%=ProjectStatus.CANCELED.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=ProjectStatus.CANCELED.toString().toLowerCase()%>
                </button>
            </form>
        </td>
    </tr>
</table>
<table>
    <%ProposalStatus consideration = ProposalStatus.CONSIDERATION;%>
    <%ProposalStatus approved = ProposalStatus.APPROVED;%>
    <%ProposalStatus rejected = ProposalStatus.REJECTED;%>
    <%ProposalStatus canceled = ProposalStatus.CANCELED;%>
    <tr>
        <td><p>Select the current status of proposals to display on page: </p></td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=consideration.toString()%>" name="<%=PROPOSAL_STATUS_PARAM%>">
                <button class="<%=consideration.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=consideration.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=approved.toString()%>" name="<%=PROPOSAL_STATUS_PARAM%>">
                <button class="<%=approved.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=approved.toString().toLowerCase()%>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=rejected.toString()%>" name="<%=PROPOSAL_STATUS_PARAM%>">
                <button class="<%=rejected.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=rejected.toString().toLowerCase()%>
                </button>
            </form>
        </td>
        <td>
            <form action="<%=actionName%>" method="get">
                <input type="hidden" value="<%=canceled.toString()%>" name="<%=PROPOSAL_STATUS_PARAM%>">
                <button class="<%=canceled.equals(status)?"btn btn-success":"btn btn-light"%>"
                        type="submit">Status <%=canceled.toString().toLowerCase()%>
                </button>
            </form>
        </td>
    </tr>
</table>
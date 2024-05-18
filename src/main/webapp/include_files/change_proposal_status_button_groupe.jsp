<%if (ProposalStatus.APPROVED.equals(status) || ProposalStatus.CONSIDERATION.equals(status)) {%>
<td>
    <form action="<%=CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=showProposalsFromChapter%>" name="<%=SHOW_PROPOSAL_LIST_BY_CHAPTER_PARAM%>">
        <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
        <input type="hidden" value="<%=ProposalStatus.REJECTED.toString()%>"
               name="<%=NEW_PROPOSAL_STATUS_PARAM%>">
        <button class="btn btn-light" type="submit">Reject</button>
    </form>
</td>
<%
    }
    if (ProposalStatus.CONSIDERATION.equals(status)) {
%>
<td>
    <form action="<%=CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=showProposalsFromChapter%>" name="<%=SHOW_PROPOSAL_LIST_BY_CHAPTER_PARAM%>">
        <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
        <input type="hidden" value="<%=ProposalStatus.APPROVED.toString()%>"
               name="<%=NEW_PROPOSAL_STATUS_PARAM%>">
        <button class="btn btn-light" type="submit">Approve</button>
    </form>
<td>
        <%}%>
        <% if (ProposalStatus.REJECTED.equals(status)) {%>
<td>
    <form action="<%=CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET%>" method="get">
        <input type="hidden" value="<%=showProposalsFromChapter%>" name="<%=SHOW_PROPOSAL_LIST_BY_CHAPTER_PARAM%>">
        <input type="hidden" value="<%=proposalDto.getId().toString()%>" name="<%=PROPOSAL_ID_PARAM%>">
        <input type="hidden" value="<%=ProposalStatus.CONSIDERATION.toString()%>"
               name="<%=NEW_PROPOSAL_STATUS_PARAM%>">
        <button class="btn btn-light" type="submit">Return to consideration status</button>
    </form>
<td>
<% } %>
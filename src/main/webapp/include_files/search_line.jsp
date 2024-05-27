<form action="<%=actionName%>" method="get">

    Search by legal Entity name: <label>
    <input name="<%=SEARCH_PARAM%>" value="
<%=request.getAttribute(SEARCH_PARAM)!=null?
    request.getAttribute(SEARCH_PARAM)
    :BLANK_STRING%>" type="text">
</label>
    <button class="btn btn-primary" type="submit">Search</button>
</form>
<%@ page import="static it.academy.util.Constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create_calculation_page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container text-center">

    <form action="<%=CREATE_CALCULATION_SERVLET%>" method="post">

        Input year(YYYY): <label>
        <input name="<%=YYYY_PARAM%>" type="text">
    </label><br/>

        Input month(MM): <label>
        <input name="<%=MM_PARAM%>" type="text">
    </label><br/>

        Input work price plan: <label>
        <input name="<%=WORK_PRICE_PLAN_PARAM%>" type="text">
    </label><br/>
        <br>
        <br>
        <input name="<%=CHAPTER_ID_PARAM%>" value="<%=request.getParameter(CHAPTER_ID_PARAM)%>" type="hidden">

        <button class="btn btn-primary" type="submit">Create new calculation</button>
    </form>
    <br>
    <form action="<%=GET_MY_CALCULATION_SERVLET%>" method="get">
        <input type="hidden" value="<%=session.getAttribute(CHAPTER_ID_PARAM)%>" name="<%=CHAPTER_ID_PARAM%>">
        <button class="btn btn-secondary" type="submit">Cancel</button>
    </form>
</div>
</body>
</html>

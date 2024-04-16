package it.academy.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.Constants.*;

@WebServlet(name = "logOutServlet", urlPatterns = SLASH_STRING + LOGOUT_SERVLET)
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        ToMainServlet.clearSession(session);

        session.removeAttribute(EMAIL_PARAM);
        session.removeAttribute(PASSWORD_PARAM);
        session.removeAttribute(ROLE_PARAM);
        session.removeAttribute(ID_PARAM);

        session.removeAttribute(TODO_PARAM);
        session.removeAttribute(DEVELOPER_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(DEVELOPER_PAGE_PARAM);
        session.removeAttribute(PROJECT_STATUS_PARAM);
        session.removeAttribute(PROJECT_PAGE_PARAM);
        session.removeAttribute(PROJECT_COUNT_ON_PAGE_PARAM);
        getServletContext().getRequestDispatcher(INDEX_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

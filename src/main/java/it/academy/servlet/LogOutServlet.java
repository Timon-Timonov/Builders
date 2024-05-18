package it.academy.servlet;

import it.academy.util.SessionCleaner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.SessionCleaner.logOutClean;
import static it.academy.util.constants.JspURLs.INDEX_JSP;
import static it.academy.util.constants.ServletURLs.LOGOUT_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "logOutServlet", urlPatterns = SLASH_STRING + LOGOUT_SERVLET)
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        SessionCleaner.clearSession(session);
        logOutClean(req);
        getServletContext().getRequestDispatcher(INDEX_JSP).forward(req, resp);
    }
}

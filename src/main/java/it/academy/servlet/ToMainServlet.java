package it.academy.servlet;

import it.academy.pojo.enums.Roles;
import it.academy.servlet.utils.SessionCleaner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.ParameterNames.ROLE_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@WebServlet(name = "toMainServlet", urlPatterns = SLASH_STRING + TO_MAIN_SERVLET)
public class ToMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Roles role = (Roles) session.getAttribute(ROLE_PARAM);

        String url;
        switch (role) {
            case DEVELOPER:
                url = DEVELOPER_PAGES_MAIN_JSP;
                break;
            case CONTRACTOR:
                url = CONTRACTOR_PAGES_MAIN_JSP;
                break;
            case ADMIN:
                url = ADMIN_PAGES_MAIN_JSP;
                break;
            default:
                url = SLASH_STRING + LOGOUT_SERVLET;
        }
        SessionCleaner.clearSession(session);

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }
}

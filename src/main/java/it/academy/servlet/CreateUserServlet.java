package it.academy.servlet;

import it.academy.pojo.enums.Roles;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.Constants.*;

@WebServlet(name = "createUserServlet", urlPatterns = SLASH_STRING + CREATE_USER_SERVLET)
public class CreateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(SELECT_NEW_USER_ROLE_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String role = req.getParameter(ROLE_PARAM);

        if (Roles.CONTRACTOR.toString().equals(role)) {
            req.getSession().setAttribute(ROLE_PARAM, Roles.CONTRACTOR);
        } else if (Roles.DEVELOPER.toString().equals(role)) {
            req.getSession().setAttribute(ROLE_PARAM, Roles.DEVELOPER);
        } else {
            Util.forwardToException1(req, resp, this, ROLE_IS_INVALID);
        }
        getServletContext().getRequestDispatcher(CREATE_USER_PAGE_JSP).forward(req, resp);
    }
}

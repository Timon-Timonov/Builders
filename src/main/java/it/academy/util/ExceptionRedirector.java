package it.academy.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.ParameterNames.MESSAGE_PARAM;

public class ExceptionRedirector {

    private ExceptionRedirector() {
    }

    public static void forwardToException1(HttpServletRequest req, HttpServletResponse resp, HttpServlet th, String message)
        throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        th.getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_PAGE_1_JSP).forward(req, resp);
    }

    public static void forwardToException2(HttpServletRequest req, HttpServletResponse resp, HttpServlet th, String message)
        throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        th.getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_CREATION_PAGE_2_JSP).forward(req, resp);
    }

    public static void forwardToException3(HttpServletRequest req, HttpServletResponse resp, HttpServlet th, String message)
        throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        th.getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
    }

}

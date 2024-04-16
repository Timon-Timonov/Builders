package it.academy.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.Constants.*;

@WebServlet(name = "redirectionServlet", urlPatterns = {SLASH_STRING + REDIRECTION_SERVLET})
public class RedirectionServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url1 = req.getParameter(URL_PARAM);
        String url = url1 != null ? url1 : LOGOUT_SERVLET;
        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }
}
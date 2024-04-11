package it.academy.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "redirectorServlet", urlPatterns = {"/redirector_servlet"})
public class RedirectorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url1 = req.getParameter("url");
        String url = url1 != null ? url1 : "/logout_servlet";
        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }
}
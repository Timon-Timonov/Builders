package it.academy.servlet;

import it.academy.pojo.enums.Roles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "createUserServlet", urlPatterns = "/create_user_servlet")
public class CreateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/select_new_user_role_page.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String role = req.getParameter("role");

        if (Roles.CONTRACTOR.toString().equals(role)) {
            req.getSession().setAttribute("role",Roles.CONTRACTOR);
        } else if (Roles.DEVELOPER.toString().equals(role)) {
            req.getSession().setAttribute("role",Roles.DEVELOPER);
        } else {
            req.setAttribute("exception_message", "Role is invalid!");
            getServletContext().getRequestDispatcher("/exception_pages/exception_page_1.jsp").forward(req, resp);
        }
        getServletContext().getRequestDispatcher("/create_user_page.jsp").forward(req, resp);
    }
}

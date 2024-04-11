package it.academy.servlet;

import it.academy.controller.AdminController;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.UserDto;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "logInServlet", urlPatterns = "/login_servlet")
public class LogInServlet extends HttpServlet {

    private final AdminController adminController = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/login_page.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email != null && !email.isEmpty()) {
            UserDto userDto = adminController.getUser(email);
            if (userDto.getId() != null) {
                if (password != null && !password.isEmpty()) {
                    if (password.equals(userDto.getPassword())) {

                        if (UserStatus.CANCELED.equals(userDto.getUserStatus())) {
                            forwardToException1(req, resp, "User has not active status. It is impossible to use this account!");
                        }
                        session.setAttribute("email", email);
                        session.setAttribute("password", password);
                        Roles role = userDto.getUserRole();
                        session.setAttribute("role", role);
                        session.setAttribute("id", userDto.getId());

                        switch (role) {
                            case CONTRACTOR:
                                getServletContext().getRequestDispatcher("/contractor_pages/main.jsp").forward(req, resp);
                                break;
                            case DEVELOPER:
                                getServletContext().getRequestDispatcher("/developer_pages/main.jsp").forward(req, resp);
                                break;
                            case ADMIN:
                                getServletContext().getRequestDispatcher("/admin_pages/main.jsp").forward(req, resp);
                                break;
                            default:
                                forwardToException1(req, resp, "Role is invalid!");
                        }
                    } else {
                        forwardToException1(req, resp, "Password is invalid!");
                    }
                } else {
                    forwardToException1(req, resp, "Password is empty!");
                }
            } else {
                forwardToException1(req, resp, "Email is invalid!");
            }
        } else {
            forwardToException1(req, resp, "Email is empty!");
        }
    }

    private void forwardToException1(HttpServletRequest req, HttpServletResponse resp, String s) throws ServletException, IOException {

        req.setAttribute("message", s);
        req.setAttribute("url","login_servlet");
        getServletContext().getRequestDispatcher("/exception_pages/exception_page_1.jsp").forward(req, resp);
    }
}

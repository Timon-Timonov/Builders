package it.academy.servlet;

import it.academy.controller.AdminController;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.UserDto;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.util.ExceptionRedirector;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.LOGIN_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "logInServlet", urlPatterns = SLASH_STRING + LOGIN_SERVLET)
public class LogInServlet extends HttpServlet {

    private final AdminController adminController = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(LOGIN_PAGE_JSP).forward(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        if (email != null && !email.isEmpty()) {
            UserDto userDto = new UserDto();
            try {
                userDto = adminController.getUser(email);
            } catch (Exception e) {
                ExceptionRedirector.forwardToException1(req, resp, this, e.getMessage());
            }
            if (userDto.getId() != null) {
                if (password != null && !password.isEmpty()) {
                    if (password.equals(userDto.getPassword())) {

                        if (UserStatus.CANCELED.equals(userDto.getUserStatus())) {
                            ExceptionRedirector.forwardToException1(req, resp, this, USER_HAS_NOT_ACTIVE_STATUS_IT_IS_IMPOSSIBLE_TO_USE_THIS_ACCOUNT);
                        }
                        session.setAttribute(EMAIL_PARAM, email);
                        session.setAttribute(PASSWORD_PARAM, password);
                        Roles role = userDto.getUserRole();
                        session.setAttribute(ROLE_PARAM, role);
                        session.setAttribute(ID_PARAM, userDto.getId());

                        switch (role) {
                            case CONTRACTOR:
                                getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_MAIN_JSP).forward(req, resp);
                                break;
                            case DEVELOPER:
                                getServletContext().getRequestDispatcher(DEVELOPER_PAGES_MAIN_JSP).forward(req, resp);
                                break;
                            case ADMIN:
                                getServletContext().getRequestDispatcher(ADMIN_PAGES_MAIN_JSP).forward(req, resp);
                                break;
                            default:
                                ExceptionRedirector.forwardToException1(req, resp, this, ROLE_IS_INVALID);
                        }
                    } else {
                        ExceptionRedirector.forwardToException1(req, resp, this, PASSWORD_IS_INVALID);
                    }
                } else {
                    ExceptionRedirector.forwardToException1(req, resp, this, PASSWORD_IS_EMPTY);
                }
            } else {
                ExceptionRedirector.forwardToException1(req, resp, this, EMAIL_IS_INVALID);
            }
        } else {
            ExceptionRedirector.forwardToException1(req, resp, this, EMAIL_IS_EMPTY);
        }
    }
}

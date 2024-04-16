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

import static it.academy.util.Constants.*;

@WebServlet(name = "logInServlet", urlPatterns = SLASH_STRING + LOGIN_SERVLET)
public class LogInServlet extends HttpServlet {

    private final AdminController adminController = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(LOGIN_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        if (email != null && !email.isEmpty()) {
            UserDto userDto = adminController.getUser(email);
            if (userDto.getId() != null) {
                if (password != null && !password.isEmpty()) {
                    if (password.equals(userDto.getPassword())) {

                        if (UserStatus.CANCELED.equals(userDto.getUserStatus())) {
                            forwardToException1(req, resp, USER_HAS_NOT_ACTIVE_STATUS_IT_IS_IMPOSSIBLE_TO_USE_THIS_ACCOUNT);
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
                                forwardToException1(req, resp, ROLE_IS_INVALID);
                        }
                    } else {
                        forwardToException1(req, resp, PASSWORD_IS_INVALID);
                    }
                } else {
                    forwardToException1(req, resp, PASSWORD_IS_EMPTY);
                }
            } else {
                forwardToException1(req, resp, EMAIL_IS_INVALID);
            }
        } else {
            forwardToException1(req, resp, EMAIL_IS_EMPTY);
        }
    }

    private void forwardToException1(HttpServletRequest req, HttpServletResponse resp, String s) throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, s);
        getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_PAGE_1_JSP).forward(req, resp);
    }
}

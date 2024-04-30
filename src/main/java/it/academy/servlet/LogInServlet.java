package it.academy.servlet;

import it.academy.controller.AdminController;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.servlet.utils.ExceptionRedirector;
import it.academy.util.converters.UserConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.servlet.utils.SessionAttributeSetter.setSessionUserData;
import static it.academy.util.constants.JspURLs.LOGIN_PAGE_JSP;
import static it.academy.util.constants.ParameterNames.EMAIL_PARAM;
import static it.academy.util.constants.ParameterNames.PASSWORD_PARAM;
import static it.academy.util.constants.ServletURLs.LOGIN_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "logInServlet", urlPatterns = SLASH_STRING + LOGIN_SERVLET)
public class LogInServlet extends HttpServlet {

    private final AdminController controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(LOGIN_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);

        LoginDto dto = controller.logIn(UserConverter.createDto(email, password));

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException1(req, resp, this, dto.getExceptionMessage());
        } else {
            setSessionUserData(req, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

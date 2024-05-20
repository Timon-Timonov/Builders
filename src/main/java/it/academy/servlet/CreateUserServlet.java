package it.academy.servlet;

import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.UserDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.SELECT_NEW_USER_ROLE_PAGE_JSP;
import static it.academy.util.constants.ParameterNames.ROLE_PARAM;
import static it.academy.util.constants.ServletURLs.CREATE_USER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createUserServlet", urlPatterns = SLASH_STRING + CREATE_USER_SERVLET)
public class CreateUserServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(SELECT_NEW_USER_ROLE_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String role = req.getParameter(ROLE_PARAM);
        DtoWithPageForUi<UserDto> dto = service.createUser(role);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException1(req, resp, this, dto.getExceptionMessage());
        } else {
            req.getSession().setAttribute(ROLE_PARAM, dto.getStatus());
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

package it.academy.servlet;

import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.UserDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionCleaner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.ROLE_PARAM;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;
import static it.academy.util.constants.ServletURLs.TO_MAIN_SERVLET;

@WebServlet(name = "toMainServlet", urlPatterns = SLASH_STRING + TO_MAIN_SERVLET)
public class ToMainServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object role = session.getAttribute(ROLE_PARAM);
        DtoWithPageForUi<UserDto> dto = service.toMainPage(role);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionCleaner.clearSession(session);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

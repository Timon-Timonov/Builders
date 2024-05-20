package it.academy.servlet.developerServlets.createServlets;

import it.academy.converters.RequestDtoConverter;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.LoginDto;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.CREATE_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_DEVELOPER_SERVLET)
public class CreateDeveloperServlet extends HttpServlet {

    private final DeveloperService service = DeveloperServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateRequestDto requestDto = RequestDtoConverter.getCreateRequestDtoCreateUser(req);
        LoginDto dto = service.createDeveloper(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException2(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setSessionUserData(req, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}









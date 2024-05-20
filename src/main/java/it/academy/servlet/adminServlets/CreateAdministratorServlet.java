package it.academy.servlet.adminServlets;

import it.academy.converters.RequestDtoConverter;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.LoginDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.CREATE_ADMIN_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createAdministratorServlet", urlPatterns = SLASH_STRING + CREATE_ADMIN_ADMINISTRATOR_SERVLET)
public class CreateAdministratorServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateRequestDto requestDto = RequestDtoConverter.getCreateRequestDtoCreateAdmin(req);

        LoginDto dto = service.createAdmin(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException2(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

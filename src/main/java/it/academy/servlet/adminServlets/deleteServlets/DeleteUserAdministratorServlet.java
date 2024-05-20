package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.converters.RequestDtoConverter;
import it.academy.dto.ChangeRequestDto;
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

import static it.academy.util.constants.ServletURLs.DELETE_USER_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "deleteUserAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_USER_ADMINISTRATOR_SERVLET)
public class DeleteUserAdministratorServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ChangeRequestDto requestDto = RequestDtoConverter.getChangeRequestDtoDeleteUser(req);
        DtoWithPageForUi<UserDto> dto = service.deleteUser(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

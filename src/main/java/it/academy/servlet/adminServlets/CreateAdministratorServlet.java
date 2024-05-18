package it.academy.servlet.adminServlets;

import it.academy.dto.CreateRequestDto;
import it.academy.dto.LoginDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.EMAIL_PARAM;
import static it.academy.util.constants.ParameterNames.PASSWORD_PARAM;
import static it.academy.util.constants.ServletURLs.CREATE_ADMIN_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createAdministratorServlet", urlPatterns = SLASH_STRING + CREATE_ADMIN_ADMINISTRATOR_SERVLET)
public class CreateAdministratorServlet extends HttpServlet {


    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);

        CreateRequestDto requestDto = CreateRequestDto.builder()
                                          .email(email)
                                          .password(password)
                                          .build();

        LoginDto dto = controller.createAdmin(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException2(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

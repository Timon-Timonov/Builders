package it.academy.servlet.developerServlets.createServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.LoginDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.servlet.utils.SessionAttributeSetter;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CREATE_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_DEVELOPER_SERVLET)
public class CreateDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        String name = req.getParameter(NAME_PARAM);
        String city = req.getParameter(CITY_PARAM);
        String street = req.getParameter(STREET_PARAM);
        String building = req.getParameter(BUILDING_PARAM);


        CreateRequestDto requestDto = CreateRequestDto.builder()
                                          .email(email)
                                          .password(password)
                                          .name(name)
                                          .city(city)
                                          .street(street)
                                          .building(building)
                                          .build();

        LoginDto dto = controller.createDeveloper(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException2(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setSessionUserData(req, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}









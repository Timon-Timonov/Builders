package it.academy.servlet.contractorServlets.createServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.LoginDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.util.SessionAttributeSetter;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CREATE_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createContractorServlet", urlPatterns = SLASH_STRING + CREATE_CONTRACTOR_SERVLET)
public class CreateContractorServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

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

        LoginDto dto = controller.createContractor(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException2(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setSessionUserData(req, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

package it.academy.servlet.developerServlets.createServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;
import it.academy.servlet.utils.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_CREATE_PROJECT_PAGE_JSP;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CREATE_PROJECT_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createProjectDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_PROJECT_DEVELOPER_SERVLET)
public class CreateProjectDeveloperServlet extends HttpServlet {


    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_PROJECT_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);

        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String city = ParameterFinder.getStringValueFromParameter(req, CITY_PARAM, BLANK_STRING);
        String street = ParameterFinder.getStringValueFromParameter(req, STREET_PARAM, BLANK_STRING);
        String building = ParameterFinder.getStringValueFromParameter(req, BUILDING_PARAM, BLANK_STRING);

        CreateRequestDto requestDto = CreateRequestDto.builder()
                                          .id(developerId)
                                          .name(projectName)
                                          .city(city)
                                          .street(street)
                                          .building(building)
                                          .build();

        DtoWithPageForUi<ProjectDto> dto = controller.createProject(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {

            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                null, null,
                PROJECT_ID_PARAM, PROJECT_NAME_PARAM, dto);
            HttpSession session = req.getSession();
            session.setAttribute(PROJECT_ADDRESS_PARAM, dto.getList().stream()
                                                            .findFirst().orElse(new ProjectDto())
                                                            .getProjectAddress().toString());

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

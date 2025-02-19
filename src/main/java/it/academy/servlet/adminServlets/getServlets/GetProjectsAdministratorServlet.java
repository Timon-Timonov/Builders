package it.academy.servlet.adminServlets.getServlets;

import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.servlet.utils.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_PROJECTS_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getProjectsAdministratorServlet", urlPatterns = SLASH_STRING + GET_PROJECTS_ADMINISTRATOR_SERVLET)
public class GetProjectsAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String developerName = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_NAME_PARAM, BLANK_STRING);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(developerId)
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .name(developerName)
                                        .build();

        DtoWithPageForUi<ProjectDto> dto = controller.getProjectsByDeveloper(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                PROJECT_PAGE_PARAM, PROJECT_COUNT_ON_PAGE_PARAM,
                DEVELOPER_ID_PARAM, DEVELOPER_NAME_PARAM, dto);

            String developerAddress = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_ADDRESS_PARAM, BLANK_STRING);
            HttpSession session = req.getSession();
            session.setAttribute(DEVELOPER_ADDRESS_PARAM, developerAddress);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}
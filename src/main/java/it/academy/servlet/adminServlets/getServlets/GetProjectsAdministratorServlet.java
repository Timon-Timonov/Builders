package it.academy.servlet.adminServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.dto.ProjectDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_PROJECTS_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getProjectsAdministratorServlet", urlPatterns = SLASH_STRING + GET_PROJECTS_ADMINISTRATOR_SERVLET)
public class GetProjectsAdministratorServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetProjectsByDeveloper(req);
        DtoWithPageForUi<ProjectDto> dto = service.getProjectsByDeveloper(filter);

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
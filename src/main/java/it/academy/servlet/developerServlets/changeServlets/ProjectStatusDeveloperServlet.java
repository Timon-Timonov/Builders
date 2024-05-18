package it.academy.servlet.developerServlets.changeServlets;

import it.academy.controller.DeveloperController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.PROJECT_STATUS_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "projectStatusDeveloperServlet", urlPatterns = SLASH_STRING + PROJECT_STATUS_DEVELOPER_SERVLET)
public class ProjectStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus newStatus = ParameterFinder.getProjectStatusFromParameter(req, NEW_PROJECT_STATUS_PARAM, null);

        FilterPageDto requestDto = FilterPageDto.builder()
                                       .id(projectId)
                                       .status(newStatus)
                                       .build();

        DtoWithPageForUi<ProjectDto> dto = controller.changeProjectStatus(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            HttpSession session = req.getSession();
            session.setAttribute(PROJECT_STATUS_PARAM, newStatus);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

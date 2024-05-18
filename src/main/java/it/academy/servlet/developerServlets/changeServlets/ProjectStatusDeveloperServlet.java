package it.academy.servlet.developerServlets.changeServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.converters.RequestDtoConverter;
import it.academy.dto.ChangeRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.ProjectDto;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.PROJECT_STATUS_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "projectStatusDeveloperServlet", urlPatterns = SLASH_STRING + PROJECT_STATUS_DEVELOPER_SERVLET)
public class ProjectStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ChangeRequestDto requestDto = RequestDtoConverter.getChangeRequestDtoChangeProjectStatus(req);
        DtoWithPageForUi<ProjectDto> dto = controller.changeProjectStatus(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

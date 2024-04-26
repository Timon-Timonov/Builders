package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "projectStatusDeveloperServlet", urlPatterns = SLASH_STRING + PROJECT_STATUS_DEVELOPER_SERVLET)
public class ProjectStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus newStatus = ParameterFinder.getProjectStatusFromParameter(req, NEW_PROJECT_STATUS_PARAM, null);
        ProjectStatus oldStatus = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, null);
        HttpSession session = req.getSession();
        try {
            switch (newStatus) {
                case IN_PROCESS:
                    if (ProjectStatus.PREPARATION.equals(oldStatus)) {
                        controller.changeProjectStatus(projectId, newStatus);
                        session.setAttribute(PROJECT_STATUS_PARAM, newStatus);
                    }
                    break;
                case COMPLETED:
                    if (ProjectStatus.IN_PROCESS.equals(oldStatus)) {
                        controller.changeProjectStatus(projectId, newStatus);
                        session.setAttribute(PROJECT_STATUS_PARAM, newStatus);
                    }
                    break;
                case CANCELED:
                    if (ProjectStatus.PREPARATION.equals(oldStatus) ||
                            ProjectStatus.IN_PROCESS.equals(oldStatus)) {
                        controller.changeProjectStatus(projectId, newStatus);
                        session.setAttribute(PROJECT_STATUS_PARAM, newStatus);
                    }
                    break;
                default:
                    ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
            }
        } catch (NotUpdateDataInDbException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, PROJECT_STATUS_NOT_UPDATE);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET).forward(req, resp);
    }
}

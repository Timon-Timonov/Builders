package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.DELETE_FAIL_PROJECT_ID;
import static it.academy.util.constants.Messages.PROJECT_NOT_DELETE;
import static it.academy.util.constants.ParameterNames.PROJECT_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "deleteProjectAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_PROJECT_ADMINISTRATOR_SERVLET)
public class DeleteProjectAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);

        try {
            controller.deleteProject(projectId);
        } catch (Exception e) {
            log.debug(DELETE_FAIL_PROJECT_ID + projectId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, PROJECT_NOT_DELETE);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_PROJECTS_ADMINISTRATOR_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

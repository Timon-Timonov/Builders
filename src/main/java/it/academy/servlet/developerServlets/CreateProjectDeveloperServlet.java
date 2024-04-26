package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.exceptions.NotCreateDataInDbException;
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
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP;
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_CREATE_PROJECT_PAGE_JSP;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.Messages.PROJECT_NOT_CREATE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CREATE_PROJECT_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "createProjectDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_PROJECT_DEVELOPER_SERVLET)
public class CreateProjectDeveloperServlet extends HttpServlet {


    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_PROJECT_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);

        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String city = ParameterFinder.getStringValueFromParameter(req, CITY_PARAM, BLANK_STRING);
        String street = ParameterFinder.getStringValueFromParameter(req, STREET_PARAM, BLANK_STRING);
        String building = ParameterFinder.getStringValueFromParameter(req, BUILDING_PARAM, BLANK_STRING);

        ProjectDto projectDto = null;
        try {
            projectDto = controller.createProject(id, projectName, city, street, building);

        } catch (NotCreateDataInDbException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, PROJECT_NOT_CREATE);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        if (projectDto == null) {
            ExceptionRedirector.forwardToException3(req, resp, this, PROJECT_NOT_CREATE);
        } else {
            HttpSession session = req.getSession();
            session.setAttribute(PROJECT_ID_PARAM, projectDto.getId());
            session.setAttribute(PROJECT_NAME_PARAM, projectDto.getProjectName());
            session.setAttribute(PROJECT_ADDRESS_PARAM, projectDto.getProjectAddress());
            session.setAttribute(PROJECT_STATUS_PARAM, projectDto.getStatus());

            getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP).forward(req, resp);
        }
    }
}

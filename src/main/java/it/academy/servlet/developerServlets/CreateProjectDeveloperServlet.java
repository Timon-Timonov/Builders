package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.Constants.*;

@Log4j2
@WebServlet(name = "createProjectDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_PROJECT_DEVELOPER_SERVLET)
public class CreateProjectDeveloperServlet extends HttpServlet {


    public static final String PROJECT_NOT_CREATE = "Project not create!";
    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_PROJECT_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);

        String projectName = Util.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String city = Util.getStringValueFromParameter(req, CITY_PARAM, BLANK_STRING);
        String street = Util.getStringValueFromParameter(req, STREET_PARAM, BLANK_STRING);
        String building = Util.getStringValueFromParameter(req, BUILDING_PARAM, BLANK_STRING);

        ProjectDto projectDto = null;
        try {
            projectDto = controller.createProject(id, projectName, city, street, building);

        } catch (NotCreateDataInDbException e) {
            Util.forwardToException3(req,resp,this,PROJECT_NOT_CREATE);
        }
        if (projectDto == null) {
            Util.forwardToException3(req,resp,this,PROJECT_NOT_CREATE);
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

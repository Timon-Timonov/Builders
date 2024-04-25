package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.Page;
import it.academy.dto.ProjectDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_PROJECTS_BY_DEVELOPER_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getMyProjectsByDeveloperServlet", urlPatterns = SLASH_STRING + GET_MY_PROJECTS_BY_DEVELOPER_CONTRACTOR_SERVLET)
public class GetMyProjectsByDeveloperServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        long developerId = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count =ParameterFinder.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String developerName = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_NAME_PARAM, BLANK_STRING);
        String developerAddress = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_ADDRESS_PARAM, BLANK_STRING);
        int developerDebt = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_DEBT_PARAM, ZERO_INT_VALUE);

        Page<ProjectDto> projectDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            projectDtoPage = controller.getMyProjectsByDeveloper(developerId, id, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        }

        page = projectDtoPage.getPageNumber();
        List<ProjectDto> projectDtoList = projectDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(PROJECT_DTO_LIST_PARAM, projectDtoList);
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(PROJECT_PAGE_PARAM, page);
        session.setAttribute(PROJECT_COUNT_ON_PAGE_PARAM, count);

        session.setAttribute(DEVELOPER_ID_PARAM, developerId);
        session.setAttribute(DEVELOPER_NAME_PARAM, developerName);
        session.setAttribute(DEVELOPER_ADDRESS_PARAM, developerAddress);
        session.setAttribute(DEVELOPER_DEBT_PARAM, developerDebt);

        session.setAttribute(SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM, true);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP).forward(req, resp);
    }
}

package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.Page;
import it.academy.dto.ProjectDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.servlet.WhatToDo;
import it.academy.util.Constants;
import it.academy.util.SessionCleaner;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.Constants.*;

@Log4j2
@WebServlet(name = "mainDeveloperServlet", urlPatterns = SLASH_STRING + MAIN_DEVELOPER_SERVLET)
public class MainDeveloperServlet extends HttpServlet {

    private final DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String toDoString = req.getParameter(TODO_PARAM);
        WhatToDo toDoNow = null;
        try {
            toDoNow = WhatToDo.valueOf(toDoString);
        } catch (IllegalArgumentException e) {
            log.debug(TODO_PARAM + toDoString, e);
        }

        if (toDoNow != null) {
            switch (toDoNow) {
                case SHOW_PROJECTS:
                    showProjects(req, resp);
                    break;
                case SHOW_CONTRACTORS:
                    showContractors(req, resp);
                    break;
                default:
                    req.setAttribute(MESSAGE_PARAM, INVALID_VALUE);
                    getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
            }
        } else {
            req.setAttribute(MESSAGE_PARAM, INVALID_VALUE);
            getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
        }
    }

    private void showContractors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        clearContractorsAttributes(req);


    }


    private void showProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearProjectAttributes(req);

        long id = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = Util.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, Constants.DEFAULT_PROJECT_STATUS);
        int page = Util.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, Constants.FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, Constants.DEFAULT_COUNT_ON_PAGE_5);

        Page<ProjectDto> projectDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);

        try {
            projectDtoPage = controller.getMyProjects(id, status, page, count);
        } catch (IOException e) {
            req.setAttribute(MESSAGE_PARAM, BAD_CONNECTION);
            getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
        }

        page = projectDtoPage.getPageNumber();
        List<ProjectDto> projectDtoList = projectDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(PROJECT_DTO_LIST_PARAM, projectDtoList);
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(PROJECT_PAGE_PARAM, page);
        session.setAttribute(PROJECT_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_PROJECTS_JSP).forward(req, resp);
    }

    private void clearContractorsAttributes(HttpServletRequest req) {
    }


}



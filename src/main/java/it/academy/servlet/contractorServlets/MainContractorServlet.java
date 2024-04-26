package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.DeveloperDto;
import it.academy.dto.Page;
import it.academy.dto.ProjectDto;
import it.academy.dto.ProposalDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.servlet.WhatToDo;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.SessionCleaner;
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

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.MAIN_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "mainContractorServlet", urlPatterns = SLASH_STRING + MAIN_CONTRACTOR_SERVLET)
public class MainContractorServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

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
                case SHOW_DEVELOPERS:
                    showDevelopers(req, resp);
                    break;
                case SHOW_PROPOSALS:
                    showProposals(req, resp);
                    break;
                case TRY_TO_CHOOSE_NEW_PROJECT:
                    chooseNewProject(req, resp);
                    break;
                default:
                    ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
            }
        } else {
            ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

    private void showDevelopers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearDeveloperAttributes(req);

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<DeveloperDto> developerDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            developerDtoPage = controller.getMyDevelopers(id, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        page = developerDtoPage.getPageNumber();
        List<DeveloperDto> developerDtoList = developerDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(DEVELOPER_DTO_LIST_PARAM, developerDtoList);
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(DEVELOPER_PAGE_PARAM, page);
        session.setAttribute(DEVELOPER_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_DEVELOPERS_JSP).forward(req, resp);
    }

    private void showProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearProjectAttributes(req);

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ProjectDto> projectDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            projectDtoPage = controller.getMyProjects(id, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        page = projectDtoPage.getPageNumber();
        List<ProjectDto> projectDtoList = projectDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(PROJECT_DTO_LIST_PARAM, projectDtoList);
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(PROJECT_PAGE_PARAM, page);
        session.setAttribute(PROJECT_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP).forward(req, resp);
    }

    private void showProposals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearProposalAttributes(req);

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ProposalDto> proposalDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            proposalDtoPage = controller.getMyProposals(id, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        page = proposalDtoPage.getPageNumber();
        List<ProposalDto> proposalDtoList = proposalDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(PROPOSAL_DTO_LIST_PARAM, proposalDtoList);
        session.setAttribute(PROPOSAL_STATUS_PARAM, status);
        session.setAttribute(PROPOSAL_PAGE_PARAM, page);
        session.setAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_PROPOSALS_JSP).forward(req, resp);
    }

    private void chooseNewProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearChapterAttributes(req);

        List<String> chapterNamesList = new ArrayList<>();
        try {
            chapterNamesList = controller.getAllChapterNames();
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        }
        req.setAttribute(CHAPTER_NAMES_LIST_PARAM, chapterNamesList);

        getServletContext().getRequestDispatcher(LIST_WITH_CHAPTER_NAMES_JSP).forward(req, resp);
    }
}

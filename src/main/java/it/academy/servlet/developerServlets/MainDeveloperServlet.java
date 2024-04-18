package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ContractorDto;
import it.academy.dto.Page;
import it.academy.dto.ProjectDto;
import it.academy.dto.ProposalDto;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
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
                case SHOW_PROPOSALS:
                    showProposals(req, resp);
                    break;
                default:
                    Util.forwardToException3(req, resp, this, INVALID_VALUE);
            }
        } else {
            Util.forwardToException3(req, resp, this, INVALID_VALUE);
        }
    }

    private void clearContractorsAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        //session.removeAttribute();
    }

    private void showContractors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        clearContractorsAttributes(req);

        long id = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = Util.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = Util.getNumberValueFromParameter(req, CONTRACTOR_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, CONTRACTOR_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ContractorDto> contractorDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            contractorDtoPage = controller.getMyContractors(id, status, page, count);
        } catch (IOException e) {
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
        }

        page = contractorDtoPage.getPageNumber();
        List<ContractorDto> contractorDtoList = contractorDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(CONTRACTOR_DTO_LIST_PARAM, contractorDtoList);
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(CONTRACTOR_PAGE_PARAM, page);
        session.setAttribute(CONTRACTOR_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CONTRACTORS_JSP).forward(req, resp);
    }

    private void showProposals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus proposalStatus = Util.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = Util.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ProposalDto> proposalDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            proposalDtoPage = controller.getAllMyProposals(id, proposalStatus, page, count);
        } catch (IOException e) {
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
        }

        page = proposalDtoPage.getPageNumber();
        List<ProposalDto> proposalDtoList = proposalDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(PROPOSAL_DTO_LIST_PARAM, proposalDtoList);
        session.setAttribute(PROPOSAL_STATUS_PARAM, proposalStatus);
        session.setAttribute(PROPOSAL_PAGE_PARAM, page);
        session.setAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_ALL_PROPOSALS_JSP).forward(req, resp);
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
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProposalStatus status = Util.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        long proposalId = Util.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus newStatus = Util.getProposalStatusFromParameter(req, PROPOSAL_NEW_STATUS_PARAM, status);

        try {
            switch (newStatus) {
                case REJECTED:
                    controller.rejectProposal(proposalId);
                    break;
                case APPROVED:
                    controller.approveProposal(proposalId);
                    break;
                case CONSIDERATION:
                    controller.considerateProposal(proposalId);
                    break;
                default:
                    Util.forwardToException3(req, resp, this, INVALID_VALUE);
            }
        } catch (NotUpdateDataInDbException e) {
            Util.forwardToException3(req, resp, this, PROPOSAL_STATUS_NOT_UPDATE);
        }
        showProposals(req, resp);
    }
}



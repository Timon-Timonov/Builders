package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ContractorDto;
import it.academy.dto.ProjectDto;
import it.academy.dto.ProposalDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;
import it.academy.servlet.utils.SessionCleaner;
import it.academy.servlet.utils.WhatToDo;
import it.academy.util.ExceptionRedirector;
import it.academy.util.constants.Constants;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Messages.INVALID_VALUE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.MAIN_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

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
                    ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
            }
        } else {
            ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
        }
    }

    private void showContractors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(developerId)
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .build();

        DtoWithPageForUi<ContractorDto> dto = controller.getMyContractors(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                CONTRACTOR_PAGE_PARAM, CONTRACTOR_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    private void showProposals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(developerId)
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .build();
        DtoWithPageForUi<ProposalDto> dto = controller.getAllMyProposals(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROPOSAL_STATUS_PARAM,
                PROPOSAL_PAGE_PARAM, PROPOSAL_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    private void showProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearProjectAttributes(req);

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, Constants.DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, Constants.FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, Constants.DEFAULT_COUNT_ON_PAGE_5);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(developerId)
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .build();

        DtoWithPageForUi<ProjectDto> dto = controller.getMyProjects(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                PROJECT_PAGE_PARAM, PROJECT_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}



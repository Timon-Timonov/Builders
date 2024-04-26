package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.servlet.WhatToDo;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.DEFAULT_PROPOSAL_STATUS;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "changeProposalStatusDeveloperServlet", urlPatterns = SLASH_STRING + CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET)
public class ChangeProposalStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus newStatus = ParameterFinder.getProposalStatusFromParameter(req, NEW_PROPOSAL_STATUS_PARAM, status);

        try {
            switch (newStatus) {
                case REJECTED:
                case APPROVED:
                case CONSIDERATION:
                    controller.changeStatusOfProposal(proposalId, newStatus);
                    break;
                default:
                    ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
            }
            if (WhatToDo.SHOW_PROPOSALS.toString().equals(req.getParameter(TODO_PARAM))) {
                getServletContext().getRequestDispatcher(SLASH_STRING + MAIN_DEVELOPER_SERVLET).forward(req, resp);
            } else {
                getServletContext().getRequestDispatcher(SLASH_STRING + GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET).forward(req, resp);
            }
        } catch (NotUpdateDataInDbException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, PROPOSAL_STATUS_NOT_UPDATE);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

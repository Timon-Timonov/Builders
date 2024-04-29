package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
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
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.ParameterNames.PROPOSAL_ID_PARAM;
import static it.academy.util.constants.ParameterNames.SHOW_PROPOSAL_LIST_BY_CONTRACTOR_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "deleteProposalAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_PROPOSAL_ADMINISTRATOR_SERVLET)
public class DeleteProposalAdministratorServlet extends HttpServlet {

    public static final String PROPOSAL_DELETE_FAILED = "Proposal delete failed!";
    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);

        try {
            controller.deleteProposal(proposalId);
        } catch (IOException e) {
            log.error(PROPOSAL_DELETE_FAILED + proposalId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (NotUpdateDataInDbException e) {
            log.error(PROPOSAL_DELETE_FAILED + proposalId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, PROPOSAL_DELETE_FAILED);
        }
        boolean showByContractor = req.getParameter(SHOW_PROPOSAL_LIST_BY_CONTRACTOR_PARAM) != null && Boolean.parseBoolean(req.getParameter(SHOW_PROPOSAL_LIST_BY_CONTRACTOR_PARAM));
        String url = showByContractor ?
                         SLASH_STRING + GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET
                         : SLASH_STRING + GET_PROPOSALS_FROM_CHAPTER_ADMINISTRATOR_SERVLET;

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}
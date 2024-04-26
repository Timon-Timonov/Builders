package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.NEW_PROPOSAL_STATUS_PARAM;
import static it.academy.util.constants.ParameterNames.PROPOSAL_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "changeProposalStatusContractorServlet", urlPatterns = SLASH_STRING + CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET)
public class ChangeProposalStatusContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus newStatus = ParameterFinder.getProposalStatusFromParameter(req, NEW_PROPOSAL_STATUS_PARAM, null);

        if (proposalId != ZERO_LONG_VALUE && newStatus != null) {
            try {
                controller.setProposalStatus(proposalId, newStatus);
            } catch (NotUpdateDataInDbException e) {
                log.error(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId, e);
                ExceptionRedirector.forwardToException3(req, resp, this, PROPOSAL_NOT_UPDATE);
            } catch (IOException e) {
                ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
            } catch (Exception e) {
                ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
            }
        } else {
            ExceptionRedirector.forwardToException3(req, resp, this, PROPOSAL_NOT_UPDATE);
        }

        getServletContext().getRequestDispatcher(SLASH_STRING + MAIN_CONTRACTOR_SERVLET).forward(req, resp);
    }
}

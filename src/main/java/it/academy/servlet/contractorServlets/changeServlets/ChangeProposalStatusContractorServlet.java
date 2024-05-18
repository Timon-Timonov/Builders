package it.academy.servlet.contractorServlets.changeServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.util.ParameterFinder;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.NEW_PROPOSAL_STATUS_PARAM;
import static it.academy.util.constants.ParameterNames.PROPOSAL_ID_PARAM;
import static it.academy.util.constants.ServletURLs.CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "changeProposalStatusContractorServlet", urlPatterns = SLASH_STRING + CHANGE_PROPOSAL_STATUS_CONTRACTOR_SERVLET)
public class ChangeProposalStatusContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus newStatus = ParameterFinder.getProposalStatusFromParameter(req, NEW_PROPOSAL_STATUS_PARAM, null);

        FilterPageDto requestDto = FilterPageDto.builder()
                                        .id(proposalId)
                                        .status(newStatus)
                                        .build();

        DtoWithPageForUi<ProposalDto> dto = controller.setProposalStatus(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

package it.academy.servlet.contractorServlets.getServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;
import it.academy.util.SessionCleaner;
import it.academy.converters.FilterRequestDtoConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_ALL_MY_PROPOSALS_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;


@WebServlet(name = "getAllMyProposalsContractorServlet", urlPatterns = SLASH_STRING + GET_ALL_MY_PROPOSALS_CONTRACTOR_SERVLET)
public class GetAllMyProposalsContractorServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterRequestDtoConverter.getPageRequestDtoShowProposals(req);
        DtoWithPageForUi<ProposalDto> dto = controller.getMyProposals(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionCleaner.clearProposalAttributes(req);
            SessionAttributeSetter.setPageData(req, PROPOSAL_STATUS_PARAM,
                PROPOSAL_PAGE_PARAM, PROPOSAL_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

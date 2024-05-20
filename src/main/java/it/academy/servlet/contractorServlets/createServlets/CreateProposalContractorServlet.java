package it.academy.servlet.contractorServlets.createServlets;

import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.converters.RequestDtoConverter;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.ProposalDto;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.CREATE_PROPOSAL_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createProposalContractorServlet", urlPatterns = SLASH_STRING + CREATE_PROPOSAL_CONTRACTOR_SERVLET)
public class CreateProposalContractorServlet extends HttpServlet {

    private final ContractorService service = ContractorServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateRequestDto requestDto = RequestDtoConverter.getCreateRequestDtoCreateProposal(req);
        DtoWithPageForUi<ProposalDto> dto = service.createProposal(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

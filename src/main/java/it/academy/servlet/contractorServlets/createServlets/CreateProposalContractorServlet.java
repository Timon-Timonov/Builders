package it.academy.servlet.contractorServlets.createServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.util.ParameterFinder;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.CHAPTER_ID_PARAM;
import static it.academy.util.constants.ParameterNames.ID_PARAM;
import static it.academy.util.constants.ServletURLs.CREATE_PROPOSAL_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createProposalContractorServlet", urlPatterns = SLASH_STRING + CREATE_PROPOSAL_CONTRACTOR_SERVLET)
public class CreateProposalContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);

        CreateRequestDto requestDto = CreateRequestDto.builder()
                                          .id(chapterId)
                                          .secondId(contractorId)
                                          .build();

        DtoWithPageForUi<ProposalDto> dto = controller.createProposal(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

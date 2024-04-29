package it.academy.servlet.contractorServlets.createServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.util.ExceptionRedirector;
import lombok.extern.log4j.Log4j2;

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

@Log4j2
@WebServlet(name = "createProposalServlet", urlPatterns = SLASH_STRING + CREATE_PROPOSAL_CONTRACTOR_SERVLET)
public class CreateProposalServlet extends HttpServlet {

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

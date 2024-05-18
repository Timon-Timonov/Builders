package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.PROPOSAL_ID_PARAM;
import static it.academy.util.constants.ParameterNames.SHOW_PROPOSAL_LIST_BY_CONTRACTOR_PARAM;
import static it.academy.util.constants.ServletURLs.DELETE_PROPOSAL_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "deleteProposalAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_PROPOSAL_ADMINISTRATOR_SERVLET)
public class DeleteProposalAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        String toDo = req.getParameter(SHOW_PROPOSAL_LIST_BY_CONTRACTOR_PARAM);

        FilterPageDto requestDto = FilterPageDto.builder()
                                        .id(proposalId)
                                        .name(toDo)
                                        .build();
        DtoWithPageForUi<ProposalDto> dto = controller.deleteProposal(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}
package it.academy.servlet.developerServlets.changeServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.util.ExceptionRedirector;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.DEFAULT_PROPOSAL_STATUS;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "changeProposalStatusDeveloperServlet", urlPatterns = SLASH_STRING + CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET)
public class ChangeProposalStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        ProposalStatus newStatus = ParameterFinder.getProposalStatusFromParameter(req, NEW_PROPOSAL_STATUS_PARAM, status);
        String whatToDo = req.getParameter(TODO_PARAM);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(proposalId)
                                        .status(newStatus)
                                        .name(whatToDo)
                                        .build();

        DtoWithPageForUi<ProposalDto> dto = controller.changeStatusOfProposal(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

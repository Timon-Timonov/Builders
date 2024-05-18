package it.academy.servlet.developerServlets.changeServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.converters.RequestDtoConverter;
import it.academy.dto.ChangeRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.ProposalDto;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "changeProposalStatusDeveloperServlet", urlPatterns = SLASH_STRING + CHANGE_PROPOSAL_STATUS_DEVELOPER_SERVLET)
public class ChangeProposalStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ChangeRequestDto requestDto = RequestDtoConverter.getChangeRequestDtoChangeProposalStatus(req);

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

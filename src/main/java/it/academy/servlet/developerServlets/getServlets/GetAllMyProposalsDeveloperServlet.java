package it.academy.servlet.developerServlets.getServlets;

import it.academy.controller.DeveloperController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;
import it.academy.converters.FilterRequestDtoConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_ALL_MY_PROPOSALS_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getAllMyProposalsServlet", urlPatterns = SLASH_STRING + GET_ALL_MY_PROPOSALS_DEVELOPER_SERVLET)
public class GetAllMyProposalsDeveloperServlet extends HttpServlet {

    private final DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterRequestDtoConverter.getPageRequestDtoShowProposals(req);
        DtoWithPageForUi<ProposalDto> dto = controller.getAllMyProposals(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROPOSAL_STATUS_PARAM,
                PROPOSAL_PAGE_PARAM, PROPOSAL_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

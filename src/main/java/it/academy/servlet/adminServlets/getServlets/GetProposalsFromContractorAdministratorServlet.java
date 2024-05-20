package it.academy.servlet.adminServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.dto.ProposalDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;


@WebServlet(name = "getProposalsFromContractorAdministratorServlet", urlPatterns = SLASH_STRING + GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET)
public class GetProposalsFromContractorAdministratorServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetProposalsByContractor(req);
        DtoWithPageForUi<ProposalDto> dto = service.getProposalsByContractorId(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {

            SessionAttributeSetter.setPageData(req, PROPOSAL_STATUS_PARAM,
                PROPOSAL_PAGE_PARAM, PROPOSAL_COUNT_ON_PAGE_PARAM,
                CONTRACTOR_ID_PARAM, null, dto);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

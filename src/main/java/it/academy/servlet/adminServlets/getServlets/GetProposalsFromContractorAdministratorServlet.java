package it.academy.servlet.adminServlets.getServlets;

import it.academy.controller.AdminController;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.Page;
import it.academy.dto.ProposalDto;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.ADMIN_PAGES_LIST_WITH_PROPOSALS_FROM_CONTRACTOR_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getProposalsFromContractorAdministratorServlet", urlPatterns = SLASH_STRING + GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET)
public class GetProposalsFromContractorAdministratorServlet extends HttpServlet {

    AdminController controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ProposalDto> proposalDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);

        try {
            proposalDtoPage = controller.getProposalsByContractorId(contractorId, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        page = proposalDtoPage.getPageNumber();
        List<ProposalDto> proposalDtoList = proposalDtoPage.getList();
        HttpSession session = req.getSession();

        req.setAttribute(PROPOSAL_DTO_LIST_PARAM, proposalDtoList);
        session.setAttribute(PROPOSAL_PAGE_PARAM, page);
        session.setAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM, count);
        session.setAttribute(PROPOSAL_STATUS_PARAM, status);
        session.setAttribute(CONTRACTOR_ID_PARAM, contractorId);

        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_PROPOSALS_FROM_CONTRACTOR_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

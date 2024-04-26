package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
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
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_LIST_WITH_PROPOSALS_OF_CHAPTER_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getMyProposalFromChapterDeveloperServlet", urlPatterns = SLASH_STRING + GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET)
public class GetMyProposalFromChapterDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);
        int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);

        Page<ProposalDto> proposalDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);

        try {
            proposalDtoPage = controller.getProposalsByChapterId(chapterId, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        page = proposalDtoPage.getPageNumber();
        List<ProposalDto> proposalDtoList = proposalDtoPage.getList();
        HttpSession session = req.getSession();

        req.setAttribute(PROPOSAL_DTO_LIST_PARAM, proposalDtoList);
        session.setAttribute(PROPOSAL_PAGE_PARAM, page);
        session.setAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM, count);
        session.setAttribute(PROPOSAL_STATUS_PARAM, status);
        session.setAttribute(CHAPTER_PRICE_PARAM, chapterPrice);
        session.setAttribute(CHAPTER_NAME_PARAM, chapterName);
        session.setAttribute(CHAPTER_ID_PARAM, chapterId);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_PROPOSALS_OF_CHAPTER_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

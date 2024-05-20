package it.academy.servlet.developerServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.dto.ProposalDto;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getMyProposalFromChapterDeveloperServlet", urlPatterns = SLASH_STRING + GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET)
public class GetMyProposalFromChapterDeveloperServlet extends HttpServlet {

    private final DeveloperService service = DeveloperServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetProposalsByChapter(req);
        DtoWithPageForUi<ProposalDto> dto = service.getProposalsByChapter(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {

            SessionAttributeSetter.setPageData(req, PROPOSAL_STATUS_PARAM,
                PROPOSAL_PAGE_PARAM, PROPOSAL_COUNT_ON_PAGE_PARAM,
                CHAPTER_ID_PARAM, CHAPTER_NAME_PARAM, dto);

            HttpSession session = req.getSession();
            int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);
            session.setAttribute(CHAPTER_PRICE_PARAM, chapterPrice);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

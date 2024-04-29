package it.academy.servlet.developerServlets.getServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ProposalDto;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;
import it.academy.util.ExceptionRedirector;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.*;
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

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(chapterId)
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .name(chapterName)
                                        .build();

        DtoWithPageForUi<ProposalDto> dto = controller.getProposalsByChapter(requestDto);

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

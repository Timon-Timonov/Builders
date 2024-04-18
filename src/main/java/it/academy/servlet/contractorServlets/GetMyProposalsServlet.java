package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.dto.Page;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.Util;
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

import static it.academy.util.Constants.*;

@Log4j2
@WebServlet(name = "getMyProposalsServlet", urlPatterns = SLASH_STRING + GET_MY_PROPOSAL_SERVLET)
public class GetMyProposalsServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long contractorId = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        String chapterName = Util.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);
        ProjectStatus status = Util.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = Util.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ChapterDto> chapterDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            chapterDtoPage = controller.getFreeChapters(contractorId, chapterName, status, page, count);
        } catch (IOException e) {
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
        }
        List<ChapterDto> chapterDtoList = chapterDtoPage.getList();
        page = chapterDtoPage.getPageNumber();

        HttpSession session = req.getSession();
        session.setAttribute(CHAPTER_NAME_PARAM, chapterName);
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(CHAPTER_COUNT_ON_PAGE_PARAM, count);
        session.setAttribute(CHAPTER_PAGE_PARAM, page);
        req.setAttribute(CHAPTER_DTO_LIST_PARAM, chapterDtoList);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_FREE_CHAPTERS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long contractorId = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        Long chapterId = Util.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);

        try {
            controller.createProposal(chapterId, contractorId);
        } catch (NotCreateDataInDbException e) {
            Util.forwardToException3(req, resp, this, PROPOSAL_NOT_CREATE);
        } catch (IOException e) {
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
        }
        doGet(req, resp);
    }
}

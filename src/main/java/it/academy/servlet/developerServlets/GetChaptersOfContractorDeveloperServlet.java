package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.dto.Page;
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
import static it.academy.util.Constants.DEVELOPER_PAGES_LIST_WITH_CHAPTERS_BY_ONE_CONTRACTOR_JSP;

@Log4j2
@WebServlet(name = "getChaptersOfContractorDeveloperServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET)
public class GetChaptersOfContractorDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long developerId = Util.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        Long contractorId = Util.getNumberValueFromParameter(req, CONTRACTOR_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = Util.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = Util.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ChapterDto> chapterDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);

        try {
            controller.getChaptersByContractorIdAndDeveloperId(developerId, contractorId, status, page, count);
        } catch (IOException e) {
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
        }

        List<ChapterDto> chapterDtoList = chapterDtoPage.getList();
        page = chapterDtoPage.getPageNumber();
        HttpSession session = req.getSession();
        req.setAttribute(CHAPTER_DTO_LIST_PARAM, chapterDtoList);
        session.setAttribute(CHAPTER_CONTRACTOR_NAME_PARAM, chapterDtoList.stream().findAny().orElse(new ChapterDto()).getContractorName());
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(CHAPTER_PAGE_PARAM, page);
        session.setAttribute(CHAPTER_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CHAPTERS_BY_ONE_CONTRACTOR_JSP).forward(req, resp);
    }
}

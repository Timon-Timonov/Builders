package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.dto.Page;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
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

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_LIST_WITH_CHAPTERS_BY_ONE_CONTRACTOR_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getChaptersOfContractorDeveloperServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET)
public class GetChaptersOfContractorDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        Long contractorId = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ChapterDto> chapterDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);

        try {
            chapterDtoPage = controller.getChaptersByContractorIdAndDeveloperId(developerId, contractorId, status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        }

        List<ChapterDto> chapterDtoList = chapterDtoPage.getList();
        page = chapterDtoPage.getPageNumber();
        HttpSession session = req.getSession();
        req.setAttribute(CHAPTER_DTO_LIST_PARAM, chapterDtoList);
        session.setAttribute(CHAPTER_CONTRACTOR_NAME_PARAM, chapterDtoList.stream().findAny().orElse(new ChapterDto()).getContractorName());
        session.setAttribute(PROJECT_STATUS_PARAM, status);
        session.setAttribute(CHAPTER_PAGE_PARAM, page);
        session.setAttribute(CHAPTER_COUNT_ON_PAGE_PARAM, count);
        session.setAttribute(CONTRACTOR_ID_PARAM, contractorId);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CHAPTERS_BY_ONE_CONTRACTOR_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

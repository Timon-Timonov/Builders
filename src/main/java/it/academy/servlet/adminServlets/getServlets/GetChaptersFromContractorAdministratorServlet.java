package it.academy.servlet.adminServlets.getServlets;

import it.academy.controller.AdminController;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.service.dto.Page;
import it.academy.util.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
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
import static it.academy.util.constants.JspURLs.ADMIN_PAGES_LIST_WITH_CHAPTERS_FROM_CONTRACTOR_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CHAPTERS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getChaptersFromContractorAdministratorServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET)
public class GetChaptersFromContractorAdministratorServlet extends HttpServlet {

    AdminController controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_ID_PARAM, ZERO_LONG_VALUE);
        int page = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ChapterDto> chapterDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            chapterDtoPage = controller.getChaptersByContractorId(contractorId, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        HttpSession session = req.getSession();
        List<ChapterDto> chapterDtoList = chapterDtoPage.getList();
        page = chapterDtoPage.getPageNumber();

        req.setAttribute(DTO_LIST_PARAM, chapterDtoList);
        session.setAttribute(CONTRACTOR_ID_PARAM, contractorId);
        session.setAttribute(CHAPTER_PAGE_PARAM, page);
        session.setAttribute(CHAPTER_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_CHAPTERS_FROM_CONTRACTOR_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

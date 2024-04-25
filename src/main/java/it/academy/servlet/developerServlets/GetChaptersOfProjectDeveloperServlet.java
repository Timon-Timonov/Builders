package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_LIST_WITH_CHAPTERS_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getChaptersOfProjectDeveloperServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET)
public class GetChaptersOfProjectDeveloperServlet extends HttpServlet {

    private final DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long projectId =ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);

        List<ChapterDto> chapterDtoList = new ArrayList<>();
        try {
            chapterDtoList = controller.getChaptersByProjectId(projectId);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        }
        HttpSession session = req.getSession();

        req.setAttribute(CHAPTER_DTO_LIST_PARAM, chapterDtoList);
        session.setAttribute(PROJECT_ID_PARAM, projectId);
        session.setAttribute(PROJECT_NAME_PARAM, projectName);
        session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CHAPTERS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

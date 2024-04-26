package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.Messages.CHAPTER_NOT_CREATE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "createChapterDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_CHAPTER_DEVELOPER_SERVLET)
public class CreateChapterDeveloperServlet extends HttpServlet {


    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING).toUpperCase();
        int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);

        try {
            controller.createChapter(projectId, chapterName, chapterPrice);
        } catch (NotCreateDataInDbException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, CHAPTER_NOT_CREATE);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET).forward(req, resp);
    }
}

package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.CHAPTER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "changeChapterStatusDeveloperServlet", urlPatterns = SLASH_STRING + CANCEL_CHAPTER_STATUS_DEVELOPER_SERVLET)
public class CancelChapterStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);

        try {
            controller.cancelChapter(chapterId);
            log.trace(CHAPTER_STATUS_CHANGED + chapterId);
        } catch (IOException e) {
            log.error(BAD_CONNECTION, e);
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (NotUpdateDataInDbException e) {
            log.error(CHANGING_OF_CHAPTER_STATUS_FAILED + chapterId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, CHANGING_OF_CHAPTER_STATUS_FAILED);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }

        getServletContext().getRequestDispatcher(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

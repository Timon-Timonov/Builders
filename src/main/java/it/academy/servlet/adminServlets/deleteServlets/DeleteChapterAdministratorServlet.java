package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.impl.AdminControllerImpl;
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
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.DELETE_FAIL_CHAPTER_ID;
import static it.academy.util.constants.ParameterNames.CHAPTER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "DeleteChapterAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_CHAPTER_ADMINISTRATOR_SERVLET)
public class DeleteChapterAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        try {
            controller.deleteChapter(chapterId);
        } catch (IOException e) {
            log.error(DELETE_FAIL_CHAPTER_ID + chapterId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            log.error(DELETE_FAIL_CHAPTER_ID + chapterId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, DELETE_FAIL_CHAPTER_ID + chapterId);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_CHAPTERS_FROM_PROJECT_ADMINISTRATOR_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.Constants.*;

@Log4j2
@WebServlet(name = "createChapterDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_CHAPTER_DEVELOPER_SERVLET)
public class CreateChapterDeveloperServlet extends HttpServlet {

    public static final String CHAPTER_NOT_CREATE = "Chapter not create!";
    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long projectId = Util.getNumberValueFromParameter(req, CITY_PARAM, ZERO_LONG_VALUE);
        String chapterName = Util.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING).toUpperCase();
        int chapterPrice = Util.getNumberValueFromParameter(req, CITY_PARAM, ZERO_INT_VALUE);

        try {
            controller.createChapter(projectId, chapterName, chapterPrice);
        } catch (NotCreateDataInDbException e) {
            Util.forwardToException3(req, resp, this, CHAPTER_NOT_CREATE);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET).forward(req, resp);
    }
}

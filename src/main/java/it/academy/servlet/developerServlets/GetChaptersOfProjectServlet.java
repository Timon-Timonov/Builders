package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static it.academy.util.Constants.*;

@WebServlet(name = "getChaptersOfProjectServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_PROJECT_SERVLET)
public class GetChaptersOfProjectServlet extends HttpServlet {

    private final DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long projectId = Long.parseLong(req.getParameter(PROJECT_ID_PARAM));

        HttpSession session = req.getSession();
        session.setAttribute(PROJECT_ID_PARAM, projectId);
        session.setAttribute(
            PROJECT_NAME_PARAM, Util.getStringValueFromParameter(req, PROJECT_NAME_PARAM, null));
        session.setAttribute(
            PROJECT_ADDRESS_PARAM, Util.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, null));

        List<ChapterDto> chapterDtoList = controller.getChaptersByProjectId(projectId);
        req.setAttribute(CHAPTER_DTO_LIST_PARAM, chapterDtoList);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CHAPTERS_JSP).forward(req, resp);
    }
}

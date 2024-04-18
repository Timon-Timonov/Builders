package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ChapterDto;
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

import static it.academy.util.Constants.*;

@WebServlet(name = "GetMyChaptersServlet", urlPatterns = SLASH_STRING + GET_MY_CHAPTERS_SERVLET)
public class GetMyChaptersServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long projectId = Util.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String projectName = Util.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String projectAddress = Util.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);
        String projectDeveloper = Util.getStringValueFromParameter(req, PROJECT_DEVELOPER_PARAM, BLANK_STRING);

        HttpSession session = req.getSession();
        Long contractorId = (Long) session.getAttribute(ID_PARAM);

        List<ChapterDto> chapterDtoList = new ArrayList<>();
        try {
            chapterDtoList = controller.getMyChaptersByProjectId(projectId, contractorId);
        } catch (IOException e) {
            Util.forwardToException3(req, resp, this, BAD_CONNECTION);
        }

        req.setAttribute(CHAPTER_DTO_LIST_PARAM, chapterDtoList);
        session.setAttribute(PROJECT_ID_PARAM, projectId);
        session.setAttribute(PROJECT_NAME_PARAM, projectName);
        session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);
        session.setAttribute(PROJECT_DEVELOPER_PARAM, projectDeveloper);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_CHAPTERS_JSP).forward(req, resp);
    }
}

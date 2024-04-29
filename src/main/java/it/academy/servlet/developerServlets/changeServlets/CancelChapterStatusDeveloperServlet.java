package it.academy.servlet.developerServlets.changeServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.util.ExceptionRedirector;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.CHAPTER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.CANCEL_CHAPTER_STATUS_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "changeChapterStatusDeveloperServlet", urlPatterns = SLASH_STRING + CANCEL_CHAPTER_STATUS_DEVELOPER_SERVLET)
public class CancelChapterStatusDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(chapterId)
                                        .build();

        DtoWithPageForUi<ChapterDto> dto = controller.cancelChapter(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

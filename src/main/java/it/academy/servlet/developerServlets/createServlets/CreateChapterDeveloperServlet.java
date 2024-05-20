package it.academy.servlet.developerServlets.createServlets;

import it.academy.converters.RequestDtoConverter;
import it.academy.dto.ChapterDto;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP;
import static it.academy.util.constants.ServletURLs.CREATE_CHAPTER_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "createChapterDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_CHAPTER_DEVELOPER_SERVLET)
public class CreateChapterDeveloperServlet extends HttpServlet {

    private final DeveloperService service = DeveloperServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateRequestDto requestDto = RequestDtoConverter.getCreateRequestDtoCreateChapter(req);
        DtoWithPageForUi<ChapterDto> dto = service.createChapter(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

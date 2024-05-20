package it.academy.servlet.developerServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.ChapterDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;


@WebServlet(name = "getChaptersOfContractorDeveloperServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET)
public class GetChaptersOfContractorDeveloperServlet extends HttpServlet {

    private final DeveloperService service = DeveloperServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetChaptersByContractorAndDeveloper(req);

        DtoWithPageForUi<ChapterDto> dto = service.getChaptersByContractorIdAndDeveloperId(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                CHAPTER_PAGE_PARAM, CHAPTER_COUNT_ON_PAGE_PARAM,
                CONTRACTOR_ID_PARAM, CHAPTER_CONTRACTOR_NAME_PARAM, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

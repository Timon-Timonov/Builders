package it.academy.servlet.developerServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.ChapterDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.service.DeveloperService;
import it.academy.service.impl.DeveloperServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getChaptersOfProjectDeveloperServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET)
public class GetChaptersOfProjectDeveloperServlet extends HttpServlet {

    private final DeveloperService service = DeveloperServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetChaptersByProject(req);
        DtoWithPageForUi<ChapterDto> dto = service.getChaptersByProject(filter);
        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, null,
                null, null,
                PROJECT_ID_PARAM, PROJECT_NAME_PARAM, dto);

            HttpSession session = req.getSession();
            String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);
            session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

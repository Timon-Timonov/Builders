package it.academy.servlet.developerServlets.getServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;


@WebServlet(name = "getChaptersOfContractorDeveloperServlet", urlPatterns = SLASH_STRING + GET_CHAPTERS_OF_CONTRACTOR_DEVELOPER_SERVLET)
public class GetChaptersOfContractorDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        long contractorId = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String contractorName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_CONTRACTOR_NAME_PARAM, BLANK_STRING);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(contractorId)
                                        .secondId(developerId)
                                        .name(contractorName)
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .build();

        DtoWithPageForUi<ChapterDto> dto = controller.getChaptersByContractorIdAndDeveloperId(requestDto);

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

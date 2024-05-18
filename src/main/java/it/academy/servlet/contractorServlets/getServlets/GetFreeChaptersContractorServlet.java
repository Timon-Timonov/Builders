package it.academy.servlet.contractorServlets.getServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_FREE_CHAPTERS_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getFreeChaptersContractorServlet", urlPatterns = SLASH_STRING + GET_FREE_CHAPTERS_CONTRACTOR_SERVLET)
public class GetFreeChaptersContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);

        FilterPageDto requestDto = FilterPageDto.builder()
                                       .id(contractorId)
                                       .status(status)
                                       .page(page)
                                       .count(count)
                                       .name(chapterName)
                                       .build();

        DtoWithPageForUi<ChapterDto> dto = controller.getFreeChapters(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {

            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                CHAPTER_PAGE_PARAM, CHAPTER_COUNT_ON_PAGE_PARAM,
                null, CHAPTER_NAME_PARAM, dto);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}
package it.academy.servlet.contractorServlets.getServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.ChapterDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_FREE_CHAPTERS_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getFreeChaptersContractorServlet", urlPatterns = SLASH_STRING + GET_FREE_CHAPTERS_CONTRACTOR_SERVLET)
public class GetFreeChaptersContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetFreeChapters(req);
        DtoWithPageForUi<ChapterDto> dto = controller.getFreeChapters(filter);

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
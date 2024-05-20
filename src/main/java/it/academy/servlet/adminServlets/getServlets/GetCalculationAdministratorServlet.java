package it.academy.servlet.adminServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.CalculationDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
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

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CALCULATION_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getCalculationAdministratorServlet", urlPatterns = SLASH_STRING + GET_CALCULATION_ADMINISTRATOR_SERVLET)
public class GetCalculationAdministratorServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetCalculationsByChapter(req);
        DtoWithPageForUi<CalculationDto> dto = service.getCalculationsByChapterId(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, null,
                CALCULATION_PAGE_PARAM, CALCULATION_COUNT_ON_PAGE_PARAM,
                CHAPTER_ID_PARAM, CHAPTER_NAME_PARAM, dto);

            String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);
            int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);
            String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);

            HttpSession session = req.getSession();
            session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);
            session.setAttribute(CHAPTER_PRICE_PARAM, chapterPrice);
            session.setAttribute(PROJECT_NAME_PARAM, projectName);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

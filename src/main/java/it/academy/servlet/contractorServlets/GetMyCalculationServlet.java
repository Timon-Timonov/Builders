package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.dto.Page;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.CONTRACTOR_PAGES_LIST_WITH_CALCULATIONS_JSP;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_CALCULATION_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getMyCalculationServlet", urlPatterns = SLASH_STRING + GET_MY_CALCULATION_CONTRACTOR_SERVLET)
public class GetMyCalculationServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        int page = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);

        Page<CalculationDto> calculationDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            calculationDtoPage = controller.getCalculationsByChapter(chapterId, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }

        List<CalculationDto> calculationDtoList = calculationDtoPage.getList();
        page = calculationDtoPage.getPageNumber();
        HttpSession session = req.getSession();

        req.setAttribute(CALCULATION_DTO_LIST_PARAM, calculationDtoList);
        session.setAttribute(CHAPTER_ID_PARAM, chapterId);
        session.setAttribute(CHAPTER_NAME_PARAM, chapterName);
        session.setAttribute(CHAPTER_PRICE_PARAM, chapterPrice);
        session.setAttribute(CALCULATION_PAGE_PARAM, page);
        session.setAttribute(CALCULATION_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_CALCULATIONS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (Boolean.TRUE.equals(req.getAttribute(EXECUTE_IN_GET_PARAM))) {
            doGet(req, resp);
        }

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int workPrice = ParameterFinder.getNumberValueFromParameter(req, WORK_PRICE_FACT_PARAM, ZERO_INT_VALUE);

        if (calculationId != ZERO_LONG_VALUE) {
            try {
                controller.updateWorkPriceFact(workPrice, calculationId);
            } catch (NotUpdateDataInDbException e) {
                ExceptionRedirector.forwardToException3(req, resp, this, CALCULATION_NOT_UPDATED);
            } catch (Exception e) {
                ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
            }
        } else {
            ExceptionRedirector.forwardToException3(req, resp, this, CALCULATION_NOT_UPDATED);
        }
        req.getSession().setAttribute(CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        doGet(req, resp);
    }
}

package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.dto.Page;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

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
import static it.academy.util.constants.JspURLs.DEVELOPER_PAGES_LIST_WITH_CALCULATIONS_JSP;
import static it.academy.util.constants.JspURLs.EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_CALCULATION_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getMyCalculationDeveloperServlet", urlPatterns = SLASH_STRING + GET_MY_CALCULATION_DEVELOPER_SERVLET)
public class GetMyCalculationDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        String chapterContractorName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_CONTRACTOR_NAME_PARAM, BLANK_STRING);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);
        int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);
        int page = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);

        Page<CalculationDto> calculationDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            calculationDtoPage = controller.getCalculationsByChapterId(chapterId, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        }
        List<CalculationDto> calculationDtoList = calculationDtoPage.getList();
        page = calculationDtoPage.getPageNumber();
        HttpSession session = req.getSession();

        req.setAttribute(CALCULATION_DTO_LIST_PARAM, calculationDtoList);
        session.setAttribute(CHAPTER_ID_PARAM, chapterId);
        session.setAttribute(CHAPTER_CONTRACTOR_NAME_PARAM, chapterContractorName);
        session.setAttribute(CHAPTER_NAME_PARAM, chapterName);
        session.setAttribute(CHAPTER_PRICE_PARAM, chapterPrice);
        session.setAttribute(CALCULATION_PAGE_PARAM, page);
        session.setAttribute(CALCULATION_COUNT_ON_PAGE_PARAM, count);

        session.setAttribute(PROJECT_NAME_PARAM, projectName);
        session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CALCULATIONS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

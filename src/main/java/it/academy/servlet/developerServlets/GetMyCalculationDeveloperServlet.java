package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.dto.Page;
import it.academy.exceptions.NotCreateDataInDbException;
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

import static it.academy.util.Constants.*;

@Log4j2
@WebServlet(name = "getMyCalculationDeveloperServlet", urlPatterns = SLASH_STRING + GET_MY_CALCULATION_DEVELOPER_SERVLET)
public class GetMyCalculationDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = Util.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        String chapterContractorName = Util.getStringValueFromParameter(req, CHAPTER_CONTRACTOR_NAME_PARAM, BLANK_STRING);
        String chapterName = Util.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);
        int chapterPrice = Util.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);
        int page = Util.getNumberValueFromParameter(req, CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, CALCULATION_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<CalculationDto> calculationDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            calculationDtoPage = controller.getCalculationsByChapterId(chapterId, page, count);
        } catch (IOException e) {
            req.setAttribute(MESSAGE_PARAM, BAD_CONNECTION);
            getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
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

        getServletContext().getRequestDispatcher(DEVELOPER_PAGES_LIST_WITH_CALCULATIONS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long calculationId = Util.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int sumAdvance = Util.getNumberValueFromParameter(req, SUM_ADVANCE_PARAM, ZERO_INT_VALUE);
        int sumForWork = Util.getNumberValueFromParameter(req, SUM_FOR_WORK_PARAM, ZERO_INT_VALUE);

        try {
            if (sumAdvance != ZERO_INT_VALUE) {
                controller.payAdvance(sumAdvance, calculationId);
            } else if (sumForWork != ZERO_INT_VALUE) {
                controller.payForWork(sumForWork, calculationId);
            }
        } catch (NotCreateDataInDbException e) {
            Util.forwardToException2(req, resp, this, NOT_SUCCESS_OPERATION);
        }
        doGet(req, resp);
    }
}

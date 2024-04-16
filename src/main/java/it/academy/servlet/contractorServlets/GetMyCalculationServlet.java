package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.dto.Page;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static it.academy.util.Constants.*;

@WebServlet(name = "getMyCalculationServlet", urlPatterns = SLASH_STRING + GET_MY_CALCULATION_SERVLET)
public class GetMyCalculationServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = Util.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        int page = Util.getNumberValueFromParameter(req, CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = Util.getNumberValueFromParameter(req, CALCULATION_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        int chapterPrice = Util.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);
        String chapterName = Util.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);

        Page<CalculationDto> calculationDtoPage = controller.getCalculationsByChapter(chapterId, page, count);
        List<CalculationDto> calculationDtoList = calculationDtoPage.getList();

        HttpSession session = req.getSession();
        session.setAttribute(CHAPTER_ID_PARAM, chapterId);
        req.setAttribute(CALCULATION_DTO_LIST_PARAM, calculationDtoList);
        session.setAttribute(CALCULATION_PAGE_PARAM, calculationDtoPage.getPageNumber());
        session.setAttribute(CALCULATION_COUNT_ON_PAGE_PARAM, count);
        session.setAttribute(CHAPTER_NAME_PARAM, chapterName);
        session.setAttribute(CHAPTER_PRICE_PARAM,chapterPrice);

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_LIST_WITH_CALCULATIONS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String calculationIdParmName = CALCULATION_ID_PARAM;
        String calculationWorkPriceFactParamName = WORK_PRICE_FACT_PARAM;

        if (req.getParameter(calculationIdParmName) != null && req.getParameter(calculationWorkPriceFactParamName) != null) {
            long calculationId;
            int workPrice;
            try {
                calculationId = Util.getNumberValueFromParameter(req, calculationIdParmName, ZERO_LONG_VALUE);
                workPrice = Util.getNumberValueFromParameter(req, calculationWorkPriceFactParamName, ZERO_INT_VALUE);
                controller.updateWorkPriceFact(workPrice, calculationId);
            } catch (NotUpdateDataInDbException e) {
                req.setAttribute(MESSAGE_PARAM, CALCULATION_NOT_UPDATED);
                getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
            } catch (NumberFormatException e) {
                req.setAttribute(MESSAGE_PARAM, INVALID_VALUE);
                getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
            }
        }
        req.getSession().setAttribute(CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        doGet(req, resp);
    }
}

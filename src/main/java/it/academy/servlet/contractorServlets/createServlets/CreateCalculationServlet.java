package it.academy.servlet.contractorServlets.createServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.JspURLs.CONTRACTOR_PAGES_CREATE_CALCULATION_JSP;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CREATE_CALCULATION_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "CreateCalculationServlet", urlPatterns = {SLASH_STRING + CREATE_CALCULATION_CONTRACTOR_SERVLET})
public class CreateCalculationServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_CREATE_CALCULATION_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        int year = ParameterFinder.getNumberValueFromParameter(req, YYYY_PARAM, ZERO_INT_VALUE);
        int month = ParameterFinder.getNumberValueFromParameter(req, MM_PARAM, ZERO_INT_VALUE);
        int workPrice = ParameterFinder.getNumberValueFromParameter(req, WORK_PRICE_PLAN_PARAM, ZERO_INT_VALUE);

        CreateRequestDto requestDto = CreateRequestDto.builder()
                                          .id(chapterId)
                                          .int1(year)
                                          .int2(month)
                                          .int3(workPrice)
                                          .build();

        DtoWithPageForUi<CalculationDto> dto = controller.createCalculation(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

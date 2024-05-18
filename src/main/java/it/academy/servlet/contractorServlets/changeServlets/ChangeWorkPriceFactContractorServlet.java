package it.academy.servlet.contractorServlets.changeServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.util.ParameterFinder;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.CALCULATION_ID_PARAM;
import static it.academy.util.constants.ParameterNames.WORK_PRICE_FACT_PARAM;
import static it.academy.util.constants.ServletURLs.CHANGE_WORK_PRICE_FACT_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "changeWorkPriceFactContractorServlet", urlPatterns = SLASH_STRING + CHANGE_WORK_PRICE_FACT_CONTRACTOR_SERVLET)
public class ChangeWorkPriceFactContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int workPrice = ParameterFinder.getNumberValueFromParameter(req, WORK_PRICE_FACT_PARAM, ZERO_INT_VALUE);

        FilterPageDto requestDto = FilterPageDto.builder()
                                        .id(calculationId)
                                        .count(workPrice)
                                        .build();

        DtoWithPageForUi<CalculationDto> dto = controller.updateWorkPriceFact(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

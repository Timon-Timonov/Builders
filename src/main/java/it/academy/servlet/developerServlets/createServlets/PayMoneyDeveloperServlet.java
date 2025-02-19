package it.academy.servlet.developerServlets.createServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.dto.CreateRequestDto;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.MoneyTransferDto;
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
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.PAY_MONEY_DEVELOPER_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "payMoneyDeveloperServlet", urlPatterns = SLASH_STRING + PAY_MONEY_DEVELOPER_SERVLET)
public class PayMoneyDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int sumAdvance = ParameterFinder.getNumberValueFromParameter(req, SUM_ADVANCE_PARAM, ZERO_INT_VALUE);
        int sumForWork = ParameterFinder.getNumberValueFromParameter(req, SUM_FOR_WORK_PARAM, ZERO_INT_VALUE);

        CreateRequestDto requestDto = CreateRequestDto.builder()
                                          .id(calculationId)
                                          .int1(sumAdvance)
                                          .int2(sumForWork)
                                          .build();

        DtoWithPageForUi<MoneyTransferDto> dto = controller.payMoney(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

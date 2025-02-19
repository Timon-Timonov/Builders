package it.academy.servlet.adminServlets.getServlets;

import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.MoneyTransferDto;
import it.academy.servlet.utils.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.CALCULATION_ID_PARAM;
import static it.academy.util.constants.ServletURLs.GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getMoneyTransferAdministratorServlet", urlPatterns = SLASH_STRING + GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET)
public class GetMoneyTransferAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(calculationId)
                                        .build();

        DtoWithPageForUi<MoneyTransferDto> dto = controller.getMoneyTransfers(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, null,
                null, null,
                CALCULATION_ID_PARAM, null, dto);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.servlet.utils.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.MONEY_TRANSFER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.DELETE_MONEY_TRANSFER_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "deleteMoneyTransferAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_MONEY_TRANSFER_ADMINISTRATOR_SERVLET)
public class DeleteMoneyTransferAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long moneyTransferId = ParameterFinder.getNumberValueFromParameter(req, MONEY_TRANSFER_ID_PARAM, ZERO_LONG_VALUE);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(moneyTransferId)
                                        .build();
        DtoWithPageForUi<ProjectDto> dto = controller.deleteMoneyTransfer(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}
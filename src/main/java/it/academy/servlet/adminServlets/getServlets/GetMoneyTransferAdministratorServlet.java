package it.academy.servlet.adminServlets.getServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.MoneyTransferDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.JspURLs.ADMIN_PAGES_LIST_WITH_MONEY_TRANSFERS_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID;
import static it.academy.util.constants.ParameterNames.CALCULATION_ID_PARAM;
import static it.academy.util.constants.ParameterNames.MONEY_TRANSFER_DTO_LIST_PARAM;
import static it.academy.util.constants.ServletURLs.GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getMoneyTransferAdministratorServlet", urlPatterns = SLASH_STRING + GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET)
public class GetMoneyTransferAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);

        List<MoneyTransferDto> moneyTransferDtoList = new ArrayList<>();
        try {
            moneyTransferDtoList = controller.getMoneyTransfers(calculationId);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + calculationId, e);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        HttpSession session = req.getSession();

        req.setAttribute(MONEY_TRANSFER_DTO_LIST_PARAM, moneyTransferDtoList);
        session.setAttribute(CALCULATION_ID_PARAM, calculationId);
        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_MONEY_TRANSFERS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

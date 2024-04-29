package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.MONEY_TRANSFER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "deleteMoneyTransferAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_MONEY_TRANSFER_ADMINISTRATOR_SERVLET)
public class DeleteMoneyTransferAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long moneyTransferId = ParameterFinder.getNumberValueFromParameter(req, MONEY_TRANSFER_ID_PARAM, ZERO_LONG_VALUE);
        try {
            controller.deleteMoneyTransfer(moneyTransferId);
        } catch (IOException e) {
            log.error(DELETE_FAIL_MONEY_TRANSFER_ID + moneyTransferId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            log.error(DELETE_FAIL_MONEY_TRANSFER_ID + moneyTransferId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, DELETE_FAIL_CHAPTER_ID + moneyTransferId);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}
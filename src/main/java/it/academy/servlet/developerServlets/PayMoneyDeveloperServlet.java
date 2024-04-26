package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.Messages.NOT_SUCCESS_OPERATION;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "payMoneyDeveloperServlet", urlPatterns = SLASH_STRING + PAY_MONEY_DEVELOPER_SERVLET)
public class PayMoneyDeveloperServlet extends HttpServlet {

    DeveloperController controller = new DeveloperControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int sumAdvance = ParameterFinder.getNumberValueFromParameter(req, SUM_ADVANCE_PARAM, ZERO_INT_VALUE);
        int sumForWork = ParameterFinder.getNumberValueFromParameter(req, SUM_FOR_WORK_PARAM, ZERO_INT_VALUE);

        try {
            if (sumAdvance != ZERO_INT_VALUE) {
                controller.payAdvance(sumAdvance, calculationId);
            } else if (sumForWork != ZERO_INT_VALUE) {
                controller.payForWork(sumForWork, calculationId);
            }
        } catch (NotCreateDataInDbException e) {
            ExceptionRedirector.forwardToException2(req, resp, this, NOT_SUCCESS_OPERATION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_MY_CALCULATION_DEVELOPER_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

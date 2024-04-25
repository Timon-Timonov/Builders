package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.DELETE_FAIL_CALCULATION_ID;
import static it.academy.util.constants.ParameterNames.CALCULATION_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "deleteCalculationAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_CALCULATION_ADMINISTRATOR_SERVLET)
public class DeleteCalculationAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        try {
            controller.deleteCalculation(calculationId);
        } catch (NotUpdateDataInDbException e) {
            log.error(DELETE_FAIL_CALCULATION_ID + calculationId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, DELETE_FAIL_CALCULATION_ID + calculationId);
        } catch (IOException e) {
            log.error(BAD_CONNECTION, e);
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_CALCULATION_ADMINISTRATOR_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

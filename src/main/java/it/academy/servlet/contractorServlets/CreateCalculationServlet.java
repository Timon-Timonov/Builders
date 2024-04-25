package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.JspURLs.CONTRACTOR_PAGES_CREATE_CALCULATION_JSP;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.Messages.CALCULATION_NOT_CREATED;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.*;

@WebServlet(name = "CreateCalculationServlet", urlPatterns = {SLASH_STRING + CREATE_CALCULATION_CONTRACTOR_SERVLET})
public class CreateCalculationServlet extends HttpServlet {


    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_CREATE_CALCULATION_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        Integer workPrice = ParameterFinder.getNumberValueFromParameter(req, WORK_PRICE_PLAN_PARAM, ZERO_INT_VALUE);
        Integer month = ParameterFinder.getNumberValueFromParameter(req, MM_PARAM, ZERO_INT_VALUE);
        Integer year = ParameterFinder.getNumberValueFromParameter(req, YYYY_PARAM, ZERO_INT_VALUE);

        try {
            controller.createCalculation(chapterId, year, month, workPrice);
        } catch (NotCreateDataInDbException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, CALCULATION_NOT_CREATED);
        }catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this,BLANK_STRING);
        }
        req.setAttribute(EXECUTE_IN_GET_PARAM, true);
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_MY_CALCULATION_CONTRACTOR_SERVLET).forward(req, resp);
    }
}

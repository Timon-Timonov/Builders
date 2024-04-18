package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.Constants.*;

@WebServlet(name = "CreateCalculationServlet", urlPatterns = {SLASH_STRING + CREATE_CALCULATION_SERVLET})
public class CreateCalculationServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_CREATE_CALCULATION_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long chapterId = Util.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        Integer workPrice = Util.getNumberValueFromParameter(req, WORK_PRICE_PLAN_PARAM, ZERO_INT_VALUE);
        Integer month = Util.getNumberValueFromParameter(req, MM_PARAM, ZERO_INT_VALUE);
        Integer year = Util.getNumberValueFromParameter(req, YYYY_PARAM, ZERO_INT_VALUE);

        try {
            controller.createCalculation(chapterId, year, month, workPrice);
        } catch (NotCreateDataInDbException e) {
            Util.forwardToException3(req, resp, this, CALCULATION_NOT_CREATED);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + GET_MY_CALCULATION_SERVLET).forward(req, resp);
    }
}

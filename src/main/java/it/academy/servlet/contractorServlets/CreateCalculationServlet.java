package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.exceptions.NotCreateDataInDbException;

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

        Long chapterId = Long.parseLong(req.getParameter(CHAPTER_ID_PARAM));
        Integer workPrice = Integer.parseInt(req.getParameter(WORK_PRICE_PLAN_PARAM));
        Integer month = Integer.parseInt(req.getParameter(MM_PARAM));
        Integer year = Integer.parseInt(req.getParameter(YYYY_PARAM));

        try {
            controller.createCalculation(chapterId, year, month, workPrice);
        } catch (NotCreateDataInDbException e) {
            req.setAttribute(MESSAGE_PARAM, CALCULATION_NOT_CREATED);
            getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
        }
        getServletContext().getRequestDispatcher("/" + GET_MY_CALCULATION_SERVLET).forward(req, resp);
    }
}

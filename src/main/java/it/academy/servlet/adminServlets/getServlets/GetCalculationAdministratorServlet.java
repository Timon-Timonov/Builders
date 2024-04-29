package it.academy.servlet.adminServlets.getServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.CalculationDto;
import it.academy.service.dto.Page;
import it.academy.util.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.ADMIN_PAGES_LIST_WITH_CALCULATIONS_JSP;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_CALCULATION_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "getCalculationAdministratorServlet", urlPatterns = SLASH_STRING + GET_CALCULATION_ADMINISTRATOR_SERVLET)
public class GetCalculationAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        int page = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);
        int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);

        Page<CalculationDto> calculationDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            calculationDtoPage = controller.getCalculationsByChapterId(chapterId, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        List<CalculationDto> calculationDtoList = calculationDtoPage.getList();
        page = calculationDtoPage.getPageNumber();
        HttpSession session = req.getSession();

        req.setAttribute(DTO_LIST_PARAM, calculationDtoList);
        session.setAttribute(CHAPTER_ID_PARAM, chapterId);
        session.setAttribute(CALCULATION_PAGE_PARAM, page);
        session.setAttribute(CALCULATION_COUNT_ON_PAGE_PARAM, count);

        session.setAttribute(PROJECT_NAME_PARAM, projectName);
        session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);
        session.setAttribute(CHAPTER_NAME_PARAM, chapterName);
        session.setAttribute(CHAPTER_PRICE_PARAM, chapterPrice);

        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_CALCULATIONS_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

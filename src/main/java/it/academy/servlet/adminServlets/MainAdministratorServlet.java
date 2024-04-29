package it.academy.servlet.adminServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.ContractorDto;
import it.academy.dto.DeveloperDto;
import it.academy.service.dto.Page;
import it.academy.dto.UserDto;
import it.academy.pojo.enums.UserStatus;
import it.academy.servlet.utils.WhatToDo;
import it.academy.util.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionCleaner;
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
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.BAD_CONNECTION;
import static it.academy.util.constants.Messages.INVALID_VALUE;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.MAIN_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "mainAdministratorServlet", urlPatterns = SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET)
public class MainAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String toDoString = req.getParameter(TODO_PARAM);
        WhatToDo toDoNow = null;
        try {
            toDoNow = WhatToDo.valueOf(toDoString);
        } catch (IllegalArgumentException e) {
            log.debug(TODO_PARAM + toDoString, e);
        }


        if (toDoNow != null) {
            switch (toDoNow) {
                case SHOW_ADMINISTRATORS:
                    showAdmins(req, resp);
                    break;
                case SHOW_CONTRACTORS:
                    showContractors(req, resp);
                    break;
                case SHOW_DEVELOPERS:
                    showDevelopers(req, resp);
                    break;
                default:
                    ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
            }
        } else {
            ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

    private void showAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        List<UserDto> userDtoList = new ArrayList<>();
        try {
            userDtoList = controller.getAllAdministrators();
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        req.setAttribute(DTO_LIST_PARAM, userDtoList);

        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_ADMINS_JSP).forward(req, resp);
    }

    private void showContractors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserStatus status = ParameterFinder.getUserStatusFromParameter(req, USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<ContractorDto> contractorDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            contractorDtoPage = controller.getAllContractors(status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        page = contractorDtoPage.getPageNumber();
        List<ContractorDto> contractorDtoList = contractorDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(DTO_LIST_PARAM, contractorDtoList);
        session.setAttribute(USER_STATUS_PARAM, status);
        session.setAttribute(CONTRACTOR_PAGE_PARAM, page);
        session.setAttribute(CONTRACTOR_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_CONTRACTORS_JSP).forward(req, resp);
    }

    private void showDevelopers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearDeveloperAttributes(req);
        UserStatus status = ParameterFinder.getUserStatusFromParameter(req, USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        Page<DeveloperDto> developerDtoPage = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER);
        try {
            developerDtoPage = controller.getAllDevelopers(status, page, count);
        } catch (IOException e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BAD_CONNECTION);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        page = developerDtoPage.getPageNumber();
        List<DeveloperDto> contractorDtoList = developerDtoPage.getList();

        HttpSession session = req.getSession();
        req.setAttribute(DTO_LIST_PARAM, contractorDtoList);
        session.setAttribute(USER_STATUS_PARAM, status);
        session.setAttribute(DEVELOPER_PAGE_PARAM, page);
        session.setAttribute(DEVELOPER_COUNT_ON_PAGE_PARAM, count);

        getServletContext().getRequestDispatcher(ADMIN_PAGES_LIST_WITH_DEVELOPERS_JSP).forward(req, resp);
    }
}

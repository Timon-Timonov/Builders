package it.academy.servlet.adminServlets;

import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.ContractorDto;
import it.academy.dto.DeveloperDto;
import it.academy.dto.UserDto;
import it.academy.pojo.enums.UserStatus;
import it.academy.servlet.utils.*;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.*;
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
            }
        }
        ExceptionRedirector.forwardToException3(req, resp, this, INVALID_VALUE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

    private void showAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DtoWithPageForUi<UserDto> dto = controller.getAllAdministrators();

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, null,
                null, null,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    private void showContractors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserStatus status = ParameterFinder.getUserStatusFromParameter(req, USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .build();

        DtoWithPageForUi<ContractorDto> dto = controller.getAllContractors(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, USER_STATUS_PARAM,
                CONTRACTOR_PAGE_PARAM, CONTRACTOR_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }

    private void showDevelopers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionCleaner.clearDeveloperAttributes(req);
        UserStatus status = ParameterFinder.getUserStatusFromParameter(req, USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .status(status)
                                        .page(page)
                                        .count(count)
                                        .build();

        DtoWithPageForUi<DeveloperDto> dto = controller.getAllDevelopers(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, USER_STATUS_PARAM,
                DEVELOPER_PAGE_PARAM, DEVELOPER_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

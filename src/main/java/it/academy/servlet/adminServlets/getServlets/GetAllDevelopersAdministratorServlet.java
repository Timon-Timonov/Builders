package it.academy.servlet.adminServlets.getServlets;

import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.DeveloperDto;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;
import it.academy.util.SessionCleaner;
import it.academy.converters.FilterPageDtoConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_ALL_DEVELOPERS_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getAllDevelopersAdministratorServlet", urlPatterns = SLASH_STRING + GET_ALL_DEVELOPERS_ADMINISTRATOR_SERVLET)
public class GetAllDevelopersAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getPageRequestDtoShowLegalEntities(req, DEVELOPER_PAGE_PARAM, DEVELOPER_COUNT_ON_PAGE_PARAM);
        DtoWithPageForUi<DeveloperDto> dto = controller.getAllDevelopers(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionCleaner.clearDeveloperAttributes(req);
            SessionAttributeSetter.setPageData(req, USER_STATUS_PARAM,
                DEVELOPER_PAGE_PARAM, DEVELOPER_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}


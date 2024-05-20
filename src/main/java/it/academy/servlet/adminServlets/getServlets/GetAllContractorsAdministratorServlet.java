package it.academy.servlet.adminServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.ContractorDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_ALL_CONTRACTORS_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getAllContractorsAdministratorServlet", urlPatterns = SLASH_STRING + GET_ALL_CONTRACTORS_ADMINISTRATOR_SERVLET)
public class GetAllContractorsAdministratorServlet extends HttpServlet {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getPageRequestDtoShowLegalEntities(req, CONTRACTOR_PAGE_PARAM, CONTRACTOR_COUNT_ON_PAGE_PARAM);
        DtoWithPageForUi<ContractorDto> dto = service.getAllContractors(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, USER_STATUS_PARAM,
                CONTRACTOR_PAGE_PARAM, CONTRACTOR_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

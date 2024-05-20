package it.academy.servlet.contractorServlets.getServlets;

import it.academy.service.ContractorService;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.service.impl.ContractorServiceImpl;
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
import static it.academy.util.constants.ServletURLs.*;

@WebServlet(name = "getAllMyDevelopersContractorServlet", urlPatterns = SLASH_STRING + GET_ALL_MY_DEVELOPERS_CONTRACTOR_SERVLET)
public class GetAllMyDevelopersContractorServlet extends HttpServlet {

    private final ContractorService service = new ContractorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getPageRequestDtoShowDevelopers(req);
        DtoWithPageForUi<DeveloperDto> dto = service.getMyDevelopers(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionCleaner.clearDeveloperAttributes(req);
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                DEVELOPER_PAGE_PARAM, DEVELOPER_COUNT_ON_PAGE_PARAM,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

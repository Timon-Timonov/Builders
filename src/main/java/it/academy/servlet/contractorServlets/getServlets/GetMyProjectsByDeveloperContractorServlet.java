package it.academy.servlet.contractorServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.dto.ProjectDto;
import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import it.academy.util.SessionAttributeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_PROJECTS_BY_DEVELOPER_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getMyProjectsByDeveloperContractorServlet", urlPatterns = SLASH_STRING + GET_MY_PROJECTS_BY_DEVELOPER_CONTRACTOR_SERVLET)
public class GetMyProjectsByDeveloperContractorServlet extends HttpServlet {

    private final ContractorService service = new ContractorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetProjectsByDeveloperAndContractor(req);

        DtoWithPageForUi<ProjectDto> dto = service.getMyProjectsByDeveloper(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                PROJECT_PAGE_PARAM, PROJECT_COUNT_ON_PAGE_PARAM,
                DEVELOPER_ID_PARAM, DEVELOPER_NAME_PARAM, dto);

            String developerAddress = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_ADDRESS_PARAM, BLANK_STRING);

            HttpSession session = req.getSession();
            session.setAttribute(DEVELOPER_ADDRESS_PARAM, developerAddress);
            session.setAttribute(SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM, true);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

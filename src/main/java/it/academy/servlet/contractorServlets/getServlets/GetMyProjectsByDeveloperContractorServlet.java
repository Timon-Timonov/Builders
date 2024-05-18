package it.academy.servlet.contractorServlets.getServlets;

import it.academy.controller.ContractorController;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.pojo.enums.ProjectStatus;
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

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_PROJECTS_BY_DEVELOPER_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "getMyProjectsByDeveloperContractorServlet", urlPatterns = SLASH_STRING + GET_MY_PROJECTS_BY_DEVELOPER_CONTRACTOR_SERVLET)
public class GetMyProjectsByDeveloperContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        long developerId = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String developerName = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_NAME_PARAM, BLANK_STRING);

        String developerAddress = ParameterFinder.getStringValueFromParameter(req, DEVELOPER_ADDRESS_PARAM, BLANK_STRING);

        FilterPageDto requestDto = FilterPageDto.builder()
                                       .id(developerId)
                                       .secondId(contractorId)
                                       .status(status)
                                       .page(page)
                                       .count(count)
                                       .name(developerName)
                                       .build();

        DtoWithPageForUi<ProjectDto> dto = controller.getMyProjectsByDeveloper(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, PROJECT_STATUS_PARAM,
                PROJECT_PAGE_PARAM, PROJECT_COUNT_ON_PAGE_PARAM,
                DEVELOPER_ID_PARAM, DEVELOPER_NAME_PARAM, dto);

            HttpSession session = req.getSession();
            session.setAttribute(DEVELOPER_ADDRESS_PARAM, developerAddress);
            session.setAttribute(SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM, true);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

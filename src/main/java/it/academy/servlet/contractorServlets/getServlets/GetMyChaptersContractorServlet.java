package it.academy.servlet.contractorServlets.getServlets;

import it.academy.converters.FilterPageDtoConverter;
import it.academy.dto.ChapterDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.FilterPageDto;
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
import static it.academy.util.constants.ServletURLs.GET_MY_CHAPTERS_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "GetMyChaptersContractorServlet", urlPatterns = SLASH_STRING + GET_MY_CHAPTERS_CONTRACTOR_SERVLET)
public class GetMyChaptersContractorServlet extends HttpServlet {

    private final ContractorService service = ContractorServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FilterPageDto filter = FilterPageDtoConverter.getFilterPageDtoGetChaptersByProjectAndContractor(req);
        DtoWithPageForUi<ChapterDto> dto = service.getMyChaptersByProjectId(filter);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, null,
                null, null,
                PROJECT_ID_PARAM, PROJECT_NAME_PARAM, dto);

            String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);
            String projectDeveloper = ParameterFinder.getStringValueFromParameter(req, PROJECT_DEVELOPER_PARAM, BLANK_STRING);

            HttpSession session = req.getSession();
            session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);
            session.setAttribute(PROJECT_DEVELOPER_PARAM, projectDeveloper);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

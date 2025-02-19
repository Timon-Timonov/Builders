package it.academy.servlet.contractorServlets.getServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ChapterDto;
import it.academy.servlet.utils.ParameterFinder;
import it.academy.servlet.utils.SessionAttributeSetter;
import it.academy.servlet.utils.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.GET_MY_CHAPTERS_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "GetMyChaptersServlet", urlPatterns = SLASH_STRING + GET_MY_CHAPTERS_CONTRACTOR_SERVLET)
public class GetMyChaptersServlet extends HttpServlet {

    private final ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        String projectAddress = ParameterFinder.getStringValueFromParameter(req, PROJECT_ADDRESS_PARAM, BLANK_STRING);
        String projectDeveloper = ParameterFinder.getStringValueFromParameter(req, PROJECT_DEVELOPER_PARAM, BLANK_STRING);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(projectId)
                                        .secondId(contractorId)
                                        .name(projectName)
                                        .build();
        DtoWithPageForUi<ChapterDto> dto = controller.getMyChaptersByProjectId(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionAttributeSetter.setPageData(req, null,
                null, null,
                PROJECT_ID_PARAM, PROJECT_NAME_PARAM, dto);

            HttpSession session = req.getSession();
            session.setAttribute(PROJECT_ADDRESS_PARAM, projectAddress);
            session.setAttribute(PROJECT_DEVELOPER_PARAM, projectDeveloper);

            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

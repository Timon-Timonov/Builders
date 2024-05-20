package it.academy.servlet.contractorServlets.createServlets;

import it.academy.converters.RequestDtoConverter;
import it.academy.dto.CalculationDto;
import it.academy.dto.CreateRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.CONTRACTOR_PAGES_CREATE_CALCULATION_JSP;
import static it.academy.util.constants.ServletURLs.CREATE_CALCULATION_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "CreateCalculationContractorServlet", urlPatterns = {SLASH_STRING + CREATE_CALCULATION_CONTRACTOR_SERVLET})
public class CreateCalculationContractorServlet extends HttpServlet {

    private final ContractorService service = ContractorServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_CREATE_CALCULATION_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateRequestDto requestDto = RequestDtoConverter.getCreateRequestDtoCreateCalculation(req);
        DtoWithPageForUi<CalculationDto> dto = service.createCalculation(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

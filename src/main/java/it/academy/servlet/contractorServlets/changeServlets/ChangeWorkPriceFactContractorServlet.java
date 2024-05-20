package it.academy.servlet.contractorServlets.changeServlets;

import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.converters.RequestDtoConverter;
import it.academy.dto.CalculationDto;
import it.academy.dto.ChangeRequestDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.util.ExceptionRedirector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.CHANGE_WORK_PRICE_FACT_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "changeWorkPriceFactContractorServlet", urlPatterns = SLASH_STRING + CHANGE_WORK_PRICE_FACT_CONTRACTOR_SERVLET)
public class ChangeWorkPriceFactContractorServlet extends HttpServlet {

   private final ContractorService service = new ContractorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ChangeRequestDto requestDto = RequestDtoConverter.getChangeRequestDtoUpdateWorkPrice(req);
        DtoWithPageForUi<CalculationDto> dto = service.updateWorkPriceFact(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

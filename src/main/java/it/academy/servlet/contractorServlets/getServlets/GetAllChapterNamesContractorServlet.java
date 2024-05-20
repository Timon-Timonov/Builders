package it.academy.servlet.contractorServlets.getServlets;

import it.academy.dto.ChapterDto;
import it.academy.dto.DtoWithPageForUi;
import it.academy.service.ContractorService;
import it.academy.service.impl.ContractorServiceImpl;
import it.academy.util.ExceptionRedirector;
import it.academy.util.SessionAttributeSetter;
import it.academy.util.SessionCleaner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.ServletURLs.GET_ALL_CHAPTER_NAMES_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;


@WebServlet(name = "getAllChapterNamesContractorServlet", urlPatterns = SLASH_STRING + GET_ALL_CHAPTER_NAMES_CONTRACTOR_SERVLET)
public class GetAllChapterNamesContractorServlet extends HttpServlet {

    private final ContractorService service = ContractorServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DtoWithPageForUi<ChapterDto> dto = service.getAllChapterNames();

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            SessionCleaner.clearChapterAttributes(req);
            SessionAttributeSetter.setPageData(req, null,
                null, null,
                null, null, dto);
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}

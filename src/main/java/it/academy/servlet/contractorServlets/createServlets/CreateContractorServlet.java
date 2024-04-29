package it.academy.servlet.contractorServlets.createServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ContractorDto;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.pojo.enums.Roles;
import it.academy.util.ExceptionRedirector;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.JspURLs.CONTRACTOR_PAGES_MAIN_JSP;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.CREATE_CONTRACTOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@Log4j2
@WebServlet(name = "createContractorServlet", urlPatterns = SLASH_STRING + CREATE_CONTRACTOR_SERVLET)
public class CreateContractorServlet extends HttpServlet {

    private final ContractorController contractorController = new ContractorControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        String name = req.getParameter(NAME_PARAM);
        String city = req.getParameter(CITY_PARAM);
        String street = req.getParameter(STREET_PARAM);
        String building = req.getParameter(BUILDING_PARAM);

        ContractorDto contractorDto = new ContractorDto();
        try {
            contractorDto = contractorController.createContractor(email, password, name, city, street, building);
        } catch (NotCreateDataInDbException e) {
            log.error(USER_NOT_CREATE);
            ExceptionRedirector.forwardToException2(req, resp, this, ACCOUNT_NOT_CREATE);
        } catch (EmailOccupaidException e) {
            log.debug(EMAIL + email + IS_OCCUPIED, e);
            ExceptionRedirector.forwardToException2(req, resp, this, EMAIL + email + IS_OCCUPIED);
        } catch (Exception e) {
            ExceptionRedirector.forwardToException3(req, resp, this, BLANK_STRING);
        }

        if (contractorDto.getId() != null) {
            HttpSession session = req.getSession();
            session.setAttribute(EMAIL_PARAM, email);
            session.setAttribute(PASSWORD_PARAM, password);
            session.setAttribute(ROLE_PARAM, Roles.CONTRACTOR);
            session.setAttribute(ID_PARAM, contractorDto.getId());
            getServletContext().getRequestDispatcher(CONTRACTOR_PAGES_MAIN_JSP).forward(req, resp);
        } else {
            ExceptionRedirector.forwardToException2(req, resp, this, ACCOUNT_NOT_CREATE);
        }
    }
}

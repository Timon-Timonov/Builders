package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.DeveloperDto;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.pojo.enums.Roles;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.Constants.*;

@Log4j2
@WebServlet(name = "createDeveloperServlet", urlPatterns = SLASH_STRING + CREATE_DEVELOPER_SERVLET)
public class CreateDeveloperServlet extends HttpServlet {

    DeveloperController developerController = new DeveloperControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        String name = req.getParameter(NAME_PARAM);
        String city = req.getParameter(CITY_PARAM);
        String street = req.getParameter(STREET_PARAM);
        String building = req.getParameter(BUILDING_PARAM);


        DeveloperDto developerDto = new DeveloperDto();
        try {
            developerDto = developerController.createDeveloper(email, password, name, city, street, building);
        } catch (NotCreateDataInDbException e) {
            forwardToException2(req, resp, ACCOUNT_NOT_CREATE);
        } catch (EmailOccupaidException e) {
            log.debug(EMAIL + email + IS_OCCUPIED, e);
            forwardToException2(req, resp, EMAIL + email + IS_OCCUPIED);
        }

        if (developerDto.getId() != null) {
            HttpSession session = req.getSession();
            session.setAttribute(EMAIL_PARAM, email);
            session.setAttribute(PASSWORD_PARAM, password);
            session.setAttribute(ROLE_PARAM, Roles.DEVELOPER);
            getServletContext().getRequestDispatcher(DEVELOPER_PAGES_MAIN_JSP).forward(req, resp);
        } else {
            forwardToException2(req, resp, ACCOUNT_NOT_CREATE);
        }
    }

    private void forwardToException2(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_CREATION_PAGE_2_JSP).forward(req, resp);
    }
}









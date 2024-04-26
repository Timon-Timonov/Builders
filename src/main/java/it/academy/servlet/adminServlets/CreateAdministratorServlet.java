package it.academy.servlet.adminServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.util.ExceptionRedirector;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ParameterNames.EMAIL_PARAM;
import static it.academy.util.constants.ParameterNames.PASSWORD_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "createAdministratorServlet", urlPatterns = SLASH_STRING + CREATE_ADMIN_ADMINISTRATOR_SERVLET)
public class CreateAdministratorServlet extends HttpServlet {


    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        try {
            controller.createAdmin(email, password);
        } catch (EmailOccupaidException e) {
            log.debug(EMAIL + email + IS_OCCUPIED, e);
            ExceptionRedirector.forwardToException3(req, resp, this, EMAIL + email + IS_OCCUPIED);
        } catch (NotCreateDataInDbException e) {
            log.error(USER_NOT_CREATE);
            ExceptionRedirector.forwardToException3(req, resp, this, ACCOUNT_NOT_CREATE);
        } catch (Exception e) {
            log.error(e);
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage());
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET).forward(req, resp);
    }
}

package it.academy.servlet.adminServlets.deleteServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.util.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.DELETE_FAIL_USER_ID;
import static it.academy.util.constants.Messages.USER_NOT_DELETE;
import static it.academy.util.constants.ParameterNames.USER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "deleteUserAdministratorServlet", urlPatterns = SLASH_STRING + DELETE_USER_ADMINISTRATOR_SERVLET)
public class DeleteUserAdministratorServlet extends HttpServlet {


    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long userId = ParameterFinder.getNumberValueFromParameter(req, USER_ID_PARAM, ZERO_LONG_VALUE);
        try {
            controller.deleteUser(userId);
        } catch (NotUpdateDataInDbException e) {
            log.debug(DELETE_FAIL_USER_ID + userId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, e.getMessage() != null ? e.getMessage() : USER_NOT_DELETE);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}

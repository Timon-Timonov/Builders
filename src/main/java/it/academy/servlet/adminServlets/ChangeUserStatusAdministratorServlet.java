package it.academy.servlet.adminServlets;

import it.academy.controller.impl.AdminControllerImpl;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.enums.UserStatus;
import it.academy.util.ExceptionRedirector;
import it.academy.util.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.DEFAULT_USER_STATUS;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.Messages.CHANGING_OF_STATUS_FAIL_USER_ID;
import static it.academy.util.constants.Messages.USER_NOT_DELETE;
import static it.academy.util.constants.ParameterNames.NEW_USER_STATUS_PARAM;
import static it.academy.util.constants.ParameterNames.USER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
@WebServlet(name = "changeUserStatusAdministratorServlet", urlPatterns = SLASH_STRING + CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET)
public class ChangeUserStatusAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long userId = ParameterFinder.getNumberValueFromParameter(req, USER_ID_PARAM, ZERO_LONG_VALUE);
        UserStatus newStatus = ParameterFinder.getUserStatusFromParameter(req, NEW_USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        try {
            controller.changeUserStatus(userId, newStatus);
        } catch (Exception e) {
            log.debug(CHANGING_OF_STATUS_FAIL_USER_ID + userId, e);
            ExceptionRedirector.forwardToException3(req, resp, this, USER_NOT_DELETE);
        }
        getServletContext().getRequestDispatcher(SLASH_STRING + MAIN_ADMINISTRATOR_SERVLET).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}


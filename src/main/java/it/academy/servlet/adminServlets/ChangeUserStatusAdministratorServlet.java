package it.academy.servlet.adminServlets;

import it.academy.controller.dto.DtoWithPageForUi;
import it.academy.controller.dto.PageRequestDto;
import it.academy.controller.impl.AdminControllerImpl;
import it.academy.dto.UserDto;
import it.academy.pojo.enums.UserStatus;
import it.academy.servlet.utils.ExceptionRedirector;
import it.academy.servlet.utils.ParameterFinder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static it.academy.util.constants.Constants.DEFAULT_USER_STATUS;
import static it.academy.util.constants.Constants.ZERO_LONG_VALUE;
import static it.academy.util.constants.ParameterNames.NEW_USER_STATUS_PARAM;
import static it.academy.util.constants.ParameterNames.USER_ID_PARAM;
import static it.academy.util.constants.ServletURLs.CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET;
import static it.academy.util.constants.ServletURLs.SLASH_STRING;

@WebServlet(name = "changeUserStatusAdministratorServlet", urlPatterns = SLASH_STRING + CHANGE_USER_STATUS_ADMINISTRATOR_SERVLET)
public class ChangeUserStatusAdministratorServlet extends HttpServlet {

    AdminControllerImpl controller = new AdminControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long userId = ParameterFinder.getNumberValueFromParameter(req, USER_ID_PARAM, ZERO_LONG_VALUE);
        UserStatus newStatus = ParameterFinder.getUserStatusFromParameter(req, NEW_USER_STATUS_PARAM, DEFAULT_USER_STATUS);

        PageRequestDto requestDto = PageRequestDto.builder()
                                        .id(userId)
                                        .status(newStatus)
                                        .build();

        DtoWithPageForUi<UserDto> dto = controller.changeUserStatus(requestDto);

        if (dto.getExceptionMessage() != null) {
            ExceptionRedirector.forwardToException3(req, resp, this, dto.getExceptionMessage());
        } else {
            getServletContext().getRequestDispatcher(dto.getUrl()).forward(req, resp);
        }
    }
}


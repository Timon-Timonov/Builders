package it.academy.util;

import it.academy.dto.DtoWithPageForUi;
import it.academy.dto.LoginDto;
import it.academy.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static it.academy.util.constants.ParameterNames.*;

public final class SessionAttributeSetter {

    private SessionAttributeSetter() {
    }

    public static void setSessionUserData(HttpServletRequest req, LoginDto dto) {

        UserDto userDto = dto.getUserFromDb();

        HttpSession session = req.getSession();
        session.setAttribute(EMAIL_PARAM, userDto.getEmail());
        session.setAttribute(PASSWORD_PARAM, userDto.getPassword());
        session.setAttribute(ROLE_PARAM, userDto.getUserRole());
        session.setAttribute(ID_PARAM, userDto.getId());
    }

    public static <T> void setPageData(

        HttpServletRequest req, String statusParamName,
        String pageParamName, String countParamName,
        String idParamName, String nameParamName,
        DtoWithPageForUi<T> dto) {

        HttpSession session = req.getSession();
        req.setAttribute(DTO_LIST_PARAM, dto.getList());

        req.setAttribute(SEARCH_PARAM, dto.getSearch());
        session.setAttribute(SEARCH_PARAM, dto.getSearch());

        if (pageParamName != null) {
            session.setAttribute(pageParamName, dto.getPage());
            session.setAttribute(countParamName, dto.getCountOnPage());
            session.setAttribute(LAST_PAGE_NUMBER_PARAM, dto.getLastPageNumber());
        }
        if (statusParamName != null) {
            session.setAttribute(statusParamName, dto.getStatus());
        }
        if (idParamName != null) {
            session.setAttribute(idParamName, dto.getId());
        }
        if (nameParamName != null) {
            session.setAttribute(nameParamName, dto.getName());
        }
    }
}

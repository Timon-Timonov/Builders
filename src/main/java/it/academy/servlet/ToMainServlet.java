package it.academy.servlet;

import it.academy.pojo.enums.Roles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.Constants.*;

@WebServlet(name = "toMainServlet", urlPatterns = SLASH_STRING + TOMAIN_SERVLET)
public class ToMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Roles role = (Roles) session.getAttribute(ROLE_PARAM);
        String url;
        switch (role) {
            case DEVELOPER:
                url = DEVELOPER_PAGES_MAIN_JSP;
                break;
            case CONTRACTOR:
                url = CONTRACTOR_PAGES_MAIN_JSP;
                break;
            case ADMIN:
                url = ADMIN_PAGES_MAIN_JSP;
                break;
            default:
                url = SLASH_STRING + LOGOUT_SERVLET;
        }
        clearSession(session);
        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    protected static void clearSession(HttpSession session) {


        session.removeAttribute(TODO_PARAM);

        session.removeAttribute(PROJECT_ID_PARAM);
        session.removeAttribute(PROJECT_PAGE_PARAM);
        session.removeAttribute(PROJECT_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(PROJECT_STATUS_PARAM);

        session.removeAttribute(PROJECT_NAME_PARAM);
        session.removeAttribute(PROJECT_ADDRESS_PARAM);
        session.removeAttribute(PROJECT_DEVELOPER_PARAM);
        session.removeAttribute(PROJECT_CONTRACTOR_PARAM);

        session.removeAttribute(DEVELOPER_ID_PARAM);
        session.removeAttribute(DEVELOPER_PAGE_PARAM);
        session.removeAttribute(DEVELOPER_COUNT_ON_PAGE_PARAM);

        session.removeAttribute(DEVELOPER_NAME_PARAM);
        session.removeAttribute(DEVELOPER_ADDRESS_PARAM);
        session.removeAttribute(DEVELOPER_DEBT_PARAM);
        session.removeAttribute(SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM);

        session.removeAttribute(CHAPTER_ID_PARAM);
        session.removeAttribute(CHAPTER_PAGE_PARAM);
        session.removeAttribute(CHAPTER_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(CHAPTER_STATUS_PARAM);
        session.removeAttribute(CHAPTER_NAME_PARAM);
        session.removeAttribute(CHAPTER_PRICE_PARAM);

        session.removeAttribute(CALCULATION_ID_PARAM);
        session.removeAttribute(CALCULATION_PAGE_PARAM);
        session.removeAttribute(CALCULATION_COUNT_ON_PAGE_PARAM);

        session.removeAttribute(PROPOSAL_ID_PARAM);
        session.removeAttribute(PROPOSAL_PAGE_PARAM);
        session.removeAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(PROPOSAL_STATUS_PARAM);
    }
}

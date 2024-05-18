package it.academy.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static it.academy.util.constants.ParameterNames.*;

public class SessionCleaner {

    private SessionCleaner() {
    }

    public static void clearProjectAttributes(HttpServletRequest req) {

        HttpSession session = req.getSession();

        session.removeAttribute(PROJECT_ID_PARAM);
        session.removeAttribute(PROJECT_NAME_PARAM);
        session.removeAttribute(PROJECT_ADDRESS_PARAM);

        session.removeAttribute(PROJECT_DEVELOPER_PARAM);
        session.removeAttribute(PROJECT_CONTRACTOR_PARAM);
        session.removeAttribute(SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM);

        session.removeAttribute(CHAPTER_ID_PARAM);
        session.removeAttribute(CHAPTER_PAGE_PARAM);
        session.removeAttribute(CHAPTER_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(CHAPTER_STATUS_PARAM);
        session.removeAttribute(CHAPTER_NAME_PARAM);
        session.removeAttribute(CHAPTER_PRICE_PARAM);
        session.removeAttribute(CHAPTER_DEBT_PARAM);
        session.removeAttribute(CHAPTER_CONTRACTOR_NAME_PARAM);

        session.removeAttribute(CALCULATION_ID_PARAM);
        session.removeAttribute(CALCULATION_PAGE_PARAM);
        session.removeAttribute(CALCULATION_COUNT_ON_PAGE_PARAM);
    }

    public static void clearDeveloperAttributes(HttpServletRequest req) {

        HttpSession session = req.getSession();
        session.removeAttribute(DEVELOPER_ID_PARAM);
        session.removeAttribute(DEVELOPER_NAME_PARAM);
        session.removeAttribute(DEVELOPER_ADDRESS_PARAM);
        session.removeAttribute(DEVELOPER_DEBT_PARAM);
    }

    public static void clearProposalAttributes(HttpServletRequest req) {

        HttpSession session = req.getSession();
        session.removeAttribute(PROPOSAL_ID_PARAM);
    }

    public static void clearChapterAttributes(HttpServletRequest req) {

        HttpSession session = req.getSession();
        session.removeAttribute(PROJECT_STATUS_PARAM);
        session.removeAttribute(CHAPTER_PAGE_PARAM);
        session.removeAttribute(CHAPTER_COUNT_ON_PAGE_PARAM);
    }

    public static void clearSession(HttpSession session) {

        session.removeAttribute(PROJECT_ID_PARAM);
        session.removeAttribute(PROJECT_PAGE_PARAM);
        session.removeAttribute(PROJECT_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(PROJECT_STATUS_PARAM);

        session.removeAttribute(PROJECT_NAME_PARAM);
        session.removeAttribute(PROJECT_ADDRESS_PARAM);
        session.removeAttribute(PROJECT_DEVELOPER_PARAM);
        session.removeAttribute(PROJECT_CONTRACTOR_PARAM);

        session.removeAttribute(DEVELOPER_ID_PARAM);
        session.removeAttribute(DEVELOPER_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(DEVELOPER_PAGE_PARAM);

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
        session.removeAttribute(CHAPTER_DEBT_PARAM);
        session.removeAttribute(CHAPTER_CONTRACTOR_NAME_PARAM);

        session.removeAttribute(CONTRACTOR_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(CONTRACTOR_PAGE_PARAM);

        session.removeAttribute(CALCULATION_ID_PARAM);
        session.removeAttribute(CALCULATION_PAGE_PARAM);
        session.removeAttribute(CALCULATION_COUNT_ON_PAGE_PARAM);

        session.removeAttribute(PROPOSAL_ID_PARAM);
        session.removeAttribute(PROPOSAL_PAGE_PARAM);
        session.removeAttribute(PROPOSAL_COUNT_ON_PAGE_PARAM);
        session.removeAttribute(PROPOSAL_STATUS_PARAM);
    }

    public static void logOutClean(HttpServletRequest req) {

        HttpSession session = req.getSession();
        session.removeAttribute(EMAIL_PARAM);
        session.removeAttribute(PASSWORD_PARAM);
        session.removeAttribute(ROLE_PARAM);
        session.removeAttribute(ID_PARAM);
    }
}

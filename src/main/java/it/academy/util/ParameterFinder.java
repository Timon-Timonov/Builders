package it.academy.util;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static it.academy.util.constants.Constants.BLANK_STRING;
import static it.academy.util.constants.Constants.NULL_STRING;
import static it.academy.util.constants.Messages.INVALID_VALUE;
import static it.academy.util.constants.ParameterNames.SEARCH_PARAM;

@Log4j2
public class ParameterFinder {

    private ParameterFinder() {
    }

    public static ProposalStatus getProposalStatusFromParameter
        (HttpServletRequest req, String parameterName, ProposalStatus defaultValue) {

        String valueFromReq = req.getParameter(parameterName);
        ProposalStatus value = null;
        if (valueFromReq != null) {
            try {
                value = ProposalStatus.valueOf(valueFromReq);
            } catch (IllegalArgumentException e) {
                log.debug(parameterName + valueFromReq, e);
            }
        }

        HttpSession session = req.getSession();
        ProposalStatus returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(parameterName) != null ?
                                     (ProposalStatus) session.getAttribute(parameterName)
                                     : defaultValue);
        } catch (Exception e) {
            log.debug(parameterName, e);
            return defaultValue;
        }
        return returnValue;
    }

    public static ProjectStatus getProjectStatusFromParameter
        (HttpServletRequest req, String parameterName, ProjectStatus defaultValue) {

        String valueFromReq = req.getParameter(parameterName);
        ProjectStatus value = null;
        if (valueFromReq != null) {
            try {
                value = ProjectStatus.valueOf(valueFromReq);
            } catch (IllegalArgumentException e) {
                log.debug(parameterName + valueFromReq, e);
            }
        }

        HttpSession session = req.getSession();
        ProjectStatus returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(parameterName) != null ?
                                     (ProjectStatus) session.getAttribute(parameterName)
                                     : defaultValue);
        } catch (Exception e) {
            log.debug(parameterName, e);
            return defaultValue;
        }
        return returnValue;
    }


    public static int getNumberValueFromParameter(
        HttpServletRequest req, String parameterName, Integer defaultValue)
        throws NumberFormatException {

        String valueFromReq = req.getParameter(parameterName);
        Integer value = null;
        try {
            value = Integer.parseInt(valueFromReq);
        } catch (NumberFormatException e) {
            log.debug(parameterName + valueFromReq, e);
        }

        HttpSession session = req.getSession();
        int returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(parameterName) != null ?
                                     (Integer) session.getAttribute(parameterName)
                                     : defaultValue);
        } catch (Exception e) {
            log.debug(parameterName, e);
            return defaultValue;
        }
        return returnValue;
    }

    public static long getNumberValueFromParameter(HttpServletRequest req, String parameterName, Long defaultValue)
        throws NumberFormatException {

        String valueFromReq = req.getParameter(parameterName);
        Long value = null;
        try {
            value = Long.parseLong(valueFromReq);
        } catch (NumberFormatException e) {
            log.debug(parameterName + valueFromReq, e);
        }

        HttpSession session = req.getSession();
        long returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(parameterName) != null ?
                                     (Long) session.getAttribute(parameterName)
                                     : defaultValue);
        } catch (Exception e) {
            log.debug(parameterName, e);
            return defaultValue;
        }
        return returnValue;
    }

    public static String getStringValueFromParameter(
        HttpServletRequest req, String parameterName, String defaultValue) {

        String valueFromReq = req.getParameter(parameterName);
        String value = null;
        if (valueFromReq != null && !valueFromReq.isBlank() && !"null".equalsIgnoreCase(valueFromReq)) {
            value = valueFromReq;
        }

        HttpSession session = req.getSession();
        String returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(parameterName) != null ?
                                     session.getAttribute(parameterName).toString()
                                     : defaultValue);
        } catch (Exception e) {
            log.debug(parameterName, e);
            return defaultValue;
        }
        return returnValue;
    }

    public static String getSearchStringValue(

        HttpServletRequest req) {

        String valueFromReq = req.getParameter(SEARCH_PARAM);
        String value = null;
        if (valueFromReq != null && !NULL_STRING.equalsIgnoreCase(valueFromReq)) {
            value = valueFromReq.isBlank() ? BLANK_STRING : valueFromReq;
        }

        HttpSession session = req.getSession();
        String returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(SEARCH_PARAM) != null ?
                                     session.getAttribute(SEARCH_PARAM).toString()
                                     : BLANK_STRING);
        } catch (Exception e) {
            log.debug(SEARCH_PARAM, e);
            return BLANK_STRING;
        }
        return returnValue;
    }

    public static UserStatus getUserStatusFromParameter(HttpServletRequest req, String parameterName, UserStatus defaultValue) {

        String valueFromReq = req.getParameter(parameterName);
        UserStatus value = null;
        if (valueFromReq != null) {
            try {
                value = UserStatus.valueOf(valueFromReq);
            } catch (IllegalArgumentException e) {
                log.error(INVALID_VALUE, e);
            }
        }

        HttpSession session = req.getSession();
        UserStatus returnValue;
        try {
            returnValue = value != null ?
                              value
                              : (session.getAttribute(parameterName) != null ?
                                     (UserStatus) session.getAttribute(parameterName)
                                     : defaultValue);
        } catch (Exception e) {
            log.debug(parameterName, e);
            return defaultValue;
        }
        return returnValue;
    }
}

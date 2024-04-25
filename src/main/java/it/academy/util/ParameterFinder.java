package it.academy.util;

import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static it.academy.util.constants.Messages.INVALID_VALUE;
@Log4j2
public class ParameterFinder {

    private ParameterFinder() {
    }

    public static ProposalStatus getProposalStatusFromParameter
        (HttpServletRequest req, String parameterName, ProposalStatus defaultValue) {

        HttpSession session = req.getSession();
        ProposalStatus status = null;
        String proposalStatusFromReq = req.getParameter(parameterName);
        if (proposalStatusFromReq != null) {
            try {
                status = ProposalStatus.valueOf(proposalStatusFromReq);
            } catch (IllegalArgumentException e) {
                log.debug(parameterName + proposalStatusFromReq, e);
            }
        }
        return status != null ?
                   status
                   : (session.getAttribute(parameterName) != null ?
                          (ProposalStatus) session.getAttribute(parameterName)
                          : defaultValue);

    }

    public static ProjectStatus getProjectStatusFromParameter
        (HttpServletRequest req, String parameterName, ProjectStatus defaultValue) {

        HttpSession session = req.getSession();
        ProjectStatus status = null;
        String projectStatusFromReq = req.getParameter(parameterName);
        if (projectStatusFromReq != null) {
            try {
                status = ProjectStatus.valueOf(projectStatusFromReq);
            } catch (IllegalArgumentException e) {
                log.debug(parameterName + projectStatusFromReq, e);
            }
        }
        return status != null ?
                   status
                   : (session.getAttribute(parameterName) != null ?
                          (ProjectStatus) session.getAttribute(parameterName)
                          : defaultValue);
    }


    public static ChapterStatus getChapterStatus
        (HttpServletRequest req, String parameterName, ChapterStatus defaultValue) {

        HttpSession session = req.getSession();
        ChapterStatus status = null;
        String chapterStatusFromReq = req.getParameter(parameterName);
        if (chapterStatusFromReq != null) {
            try {
                status = ChapterStatus.valueOf(chapterStatusFromReq);
            } catch (IllegalArgumentException e) {
                log.debug(parameterName + chapterStatusFromReq, e);
            }
        }
        return status != null ?
                   status
                   : (session.getAttribute(parameterName) != null ?
                          (ChapterStatus) session.getAttribute(parameterName)
                          : defaultValue);
    }

    public static int getNumberValueFromParameter(HttpServletRequest req, String parameterName, Integer
                                                                                                    defaultValue)
        throws NumberFormatException {

        String valueFromReq = req.getParameter(parameterName);
        HttpSession session = req.getSession();
        return valueFromReq != null ?
                   Integer.parseInt(valueFromReq)
                   : (session.getAttribute(parameterName) != null ?
                          (Integer) session.getAttribute(parameterName)
                          : defaultValue);
    }

    public static long getNumberValueFromParameter(HttpServletRequest req, String parameterName, Long defaultValue)
        throws NumberFormatException {

        String valueFromReq = req.getParameter(parameterName);
        HttpSession session = req.getSession();
        return valueFromReq != null ?
                   Long.parseLong(valueFromReq)
                   : (session.getAttribute(parameterName) != null ?
                          (Long) session.getAttribute(parameterName)
                          : defaultValue);
    }

    public static String getStringValueFromParameter(
        HttpServletRequest req, String parameterName, String defaultValue) {

        String valueFromReq = req.getParameter(parameterName);
        HttpSession session = req.getSession();
        return valueFromReq != null ?
                   valueFromReq
                   : (session.getAttribute(parameterName) != null ?
                          session.getAttribute(parameterName).toString()
                          : defaultValue);
    }

    public static UserStatus getUserStatusFromParameter(HttpServletRequest req, String parameterName, UserStatus defaultValue) {

        String valueFromReq = req.getParameter(parameterName);
        HttpSession session = req.getSession();
        UserStatus status = null;
        if (valueFromReq != null) {
            try {
                status = UserStatus.valueOf(valueFromReq);
            } catch (IllegalArgumentException e) {
                log.error(INVALID_VALUE, e);
            }
        }
        return status != null ?
                   status
                   : (session.getAttribute(parameterName) != null ?
                          (UserStatus) session.getAttribute(parameterName)
                          : defaultValue);
    }

}

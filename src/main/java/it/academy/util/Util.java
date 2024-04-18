package it.academy.util;

import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.*;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static it.academy.util.Constants.*;

@Log4j2
public class Util {

    private Util() {
    }

    public static int getCorrectPageNumber(Integer page, int count, long totalCount) {

        if (page == ZERO_PAGE_NUMBER || page == FIRST_PAGE_NUMBER) {
            return FIRST_PAGE_NUMBER;
        }
        int lastPage = totalCount == 0L ?
                           FIRST_PAGE_NUMBER
                           : ((totalCount % count == 0) ?
                                  (int) (totalCount / count)
                                  : (int) (1 + (totalCount / count)));
        if (page == DEFAULT_LAST_PAGE_NUMBER || page > lastPage) {
            page = lastPage;
        }
        return page;
    }

    public static Integer[] getDebtFromCalculation(Calculation calculation) {

        if (calculation == null) {
            return new Integer[]{0, 0, 0};
        }
        AtomicReference<Integer> sumAdvance = new AtomicReference<>(0);
        AtomicReference<Integer> sumForWork = new AtomicReference<>(0);
        calculation.getTransferSet()
            .forEach(moneyTransfer -> {
                if (PaymentType.ADVANCE_PAYMENT.equals(moneyTransfer.getType())) {
                    sumAdvance.updateAndGet(v -> v + moneyTransfer.getSum());
                } else if (PaymentType.PAYMENT_FOR_WORK.equals(moneyTransfer.getType())) {
                    sumForWork.updateAndGet(v -> v + moneyTransfer.getSum());
                }
            });
        int workPriceFact = calculation.getWorkPriceFact() == null ? 0 : calculation.getWorkPriceFact();
        int calculationDebt = workPriceFact - sumAdvance.get() - sumForWork.get();
        return new Integer[]{calculationDebt, sumAdvance.get(), sumForWork.get()};
    }

    public static Integer getDebtByChapter(Chapter chapter) {

        if (chapter == null) {
            return 0;
        }
        return chapter.getCalculationSet()
                   .stream()
                   .map(Util::getDebtFromCalculation)
                   .map(integers -> integers[0])
                   .reduce(0, Integer::sum);
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
                          (String) session.getAttribute(parameterName)
                          : defaultValue);
    }

    public static UserStatus getUserStatus(HttpServletRequest req, String parameterName, UserStatus defaultValue) {

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

    public static void forwardToException1(HttpServletRequest req, HttpServletResponse resp, HttpServlet th, String message)
        throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        th.getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_PAGE_1_JSP).forward(req, resp);
    }

    public static void forwardToException2(HttpServletRequest req, HttpServletResponse resp, HttpServlet th, String message)
        throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        th.getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_CREATION_PAGE_2_JSP).forward(req, resp);
    }

    public static void forwardToException3(HttpServletRequest req, HttpServletResponse resp, HttpServlet th, String message)
        throws ServletException, IOException {

        req.setAttribute(MESSAGE_PARAM, message);
        th.getServletContext().getRequestDispatcher(EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP).forward(req, resp);
    }
}

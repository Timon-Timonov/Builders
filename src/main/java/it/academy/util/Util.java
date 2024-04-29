package it.academy.util;

import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.MoneyTransfer;
import it.academy.pojo.Project;
import it.academy.pojo.enums.PaymentType;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

import static it.academy.util.constants.Constants.*;

@Log4j2
public class Util {

    private Util() {
    }

    public static int getCorrectPageNumber(int page, int count, long totalCount) {

        if (page == ZERO_PAGE_NUMBER || page == FIRST_PAGE_NUMBER) {
            return FIRST_PAGE_NUMBER;
        }
        int lastPage = totalCount == 0L ?
                           FIRST_PAGE_NUMBER
                           : ((totalCount % count == ZERO_INT_VALUE) ?
                                  (int) (totalCount / count)
                                  : (int) (1 + (totalCount / count)));
        if (page == DEFAULT_LAST_PAGE_NUMBER || page > lastPage) {
            page = lastPage;
        }
        return page;
    }

    public static Integer[] getCalculationSums(Calculation calculation) {

        if (calculation == null) {
            return new Integer[]{ZERO_INT_VALUE, ZERO_INT_VALUE, ZERO_INT_VALUE};
        }
        Set<MoneyTransfer> transferSet = calculation.getTransferSet();

        int sumAdvance = transferSet.stream()
                             .filter(moneyTransfer -> PaymentType.ADVANCE_PAYMENT.equals(moneyTransfer.getType()))
                             .map(MoneyTransfer::getSum)
                             .reduce(ZERO_INT_VALUE, Integer::sum);
        int sumForWork = transferSet.stream()
                             .filter(moneyTransfer -> PaymentType.PAYMENT_FOR_WORK.equals(moneyTransfer.getType()))
                             .map(MoneyTransfer::getSum)
                             .reduce(ZERO_INT_VALUE, Integer::sum);

        int workPriceFact = calculation.getWorkPriceFact() == null ? 0 : calculation.getWorkPriceFact();
        int calculationDebt = workPriceFact - sumAdvance - sumForWork;
        return new Integer[]{calculationDebt, sumAdvance, sumForWork};
    }

    public static Integer getDebtByChapter(Chapter chapter) {

        if (chapter == null) {
            return ZERO_INT_VALUE;
        }
        return chapter.getCalculationSet()
                   .stream()
                   .map(Util::getCalculationSums)
                   .map(integers -> integers[0])
                   .reduce(ZERO_INT_VALUE, Integer::sum);
    }

    public static int getProjectDept(Project project) {

        return project.getChapters()
                   .stream()
                   .map(Util::getDebtByChapter)
                   .reduce(ZERO_INT_VALUE, Integer::sum);
    }

}

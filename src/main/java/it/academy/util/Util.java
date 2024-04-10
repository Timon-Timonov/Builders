package it.academy.util;

import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.PaymentType;

import java.util.concurrent.atomic.AtomicReference;

import static it.academy.util.Constants.*;

public class Util {

    private Util() {
    }

    public static int getCorrectPageNumber(int page, int count, long totalCount) {

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
        int calculationDebt = calculation.getWorkPriceFact() - sumAdvance.get() - sumForWork.get();
        return new Integer[]{calculationDebt, sumAdvance.get(), sumForWork.get()};
    }

    public static Integer getDebtByChapter(Chapter chapter) {

        return chapter.getCalculationSet()
                   .stream()
                   .map(Util::getDebtFromCalculation)
                   .map(integers -> integers[2])
                   .reduce(0, Integer::sum);
    }
}

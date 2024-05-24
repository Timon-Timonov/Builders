package it.academy.util;

import it.academy.dto.Page;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

import static it.academy.util.constants.Constants.*;

@Log4j2
public class Util {

    private Util() {
    }

    public static <T> Page<T> getPageWithCorrectNumbers(int page, int count, long totalCount) {

        int lastPage = totalCount == ZERO_LONG_VALUE ?
                           FIRST_PAGE_NUMBER
                           : ((totalCount % count == ZERO_INT_VALUE) ?
                                  (int) (totalCount / count)
                                  : (int) (1 + (totalCount / count)));

        int correctPage = FIRST_PAGE_NUMBER;
        if (page < ZERO_PAGE_NUMBER || page > lastPage) {
            correctPage = lastPage;
        } else if (page != ZERO_PAGE_NUMBER) {
            correctPage = page;
        }
        return new Page<>(new ArrayList<>(), correctPage, lastPage);
    }
}

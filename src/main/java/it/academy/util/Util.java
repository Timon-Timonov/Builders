package it.academy.util;

import static it.academy.util.Constants.*;

public class Util {

    private Util() {
    }

    public static int getCorrectPageNumber(int page, int count, int totalCount) {

        if (page == ZERO_PAGE_NUMBER) {
            return FIRST_PAGE_NUMBER;
        }
        int lastPage = (totalCount % count == 0) ?
                           (totalCount / count)
                           : (1 + (totalCount / count));
        if (page == DEFAULT_LAST_PAGE_NUMBER || page > lastPage) {
            page = lastPage;
        }
        return page;
    }
}

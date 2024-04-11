package it.academy.util;

import it.academy.pojo.enums.ProjectStatus;

public final class Constants {

    public static final String SELECT_ALL_FROM_TABLE = "SELECT c FROM %s c";
    public static final String SELECT_COUNT_FROM_TABLE = "SELECT COUNT (c) FROM %s c";

    public static final int COUNT_ON_PAGE_3 = 3;
    public static final int DEFAULT_COUNT_ON_PAGE_5 = 5;
    public static final int COUNT_ON_PAGE_10 = 10;

    public static final int ZERO_PAGE_NUMBER = 0;
    public static final int FIRST_PAGE_NUMBER = 1;
    public static final int DEFAULT_LAST_PAGE_NUMBER = -1;

    public static final ProjectStatus DEFAULT_PROJECT_STATUS=ProjectStatus.IN_PROCESS;
    public static final String GET_MY_CHAPTERS_SERVLET = "get_my_chapters_servlet";
    public static final String MAIN_CONTRACTOR_SERVLET = "main_contractor_servlet";

    private Constants() {
    }
}

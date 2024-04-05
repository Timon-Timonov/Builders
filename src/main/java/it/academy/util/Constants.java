package it.academy.util;

public final class Constants {

    public static final String SELECT_ALL_FROM_TABLE = "SELECT c FROM %s c";
    public static final String SELECT_COUNT_FROM_TABLE = "SELECT COUNT (c) FROM %s c";

    public static final int COUNT_ON_PAGE_3 = 3;
    public static final int DEFAULT_COUNT_ON_PAGE = 5;
    public static final int COUNT_ON_PAGE_10 = 10;

    public static final int ZERO_PAGE_NUMBER = 0;
    public static final int FIRST_PAGE_NUMBER = 1;
    public static final int DEFAULT_LAST_PAGE_NUMBER = -1;

    private Constants() {
    }
}

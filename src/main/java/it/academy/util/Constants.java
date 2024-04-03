package it.academy.util;

public final class Constants {

    public static final String SELECT_ALL_FROM_TABLE = "SELECT c FROM %s c";
    public static final String SELECT_COUNT_FROM_TABLE = "SELECT COUNT (c) FROM %s c";
    public static final String SELECT_ALL_FROM_STUDENT_WITH_ORDER =
        "SELECT s FROM Student s ORDER BY s.surname ASC , s.name ASC, s.age ASC";
    public static final String SELECT_FROM_COUNTRY_ORDER_BY_COUNTRYNAME_ASC =
        "SELECT c FROM Country c ORDER BY с.countryName ASC ";
    public static final String SELECT_S_FROM_STUDENT_BY_COUNTRY_ID =
        "SELECT s FROM Student s ,Country c  WHERE s.country=c.id AND c.id=%d ORDER BY s.surname ASC , s.name ASC, s.age ASC";
    public static final String DELETE_ALL_FROM_TABLE = "DELETE FROM %s";
    public static final String SELECT_C_FROM_COUNTRY_C_ORDER_BY_C_COUNTRY_NAME_ASC =
        "SELECT c FROM Country c ORDER BY c.countryName ASC";
    public static final String SELECT_COUNT_STUDENT_FROM_COUNTRY_BY_CONTRYID =
        "SELECT COUNT (s) FROM Student s,Country c WHERE c.id=%d AND s member c.students";
    public static final int DEFAULT_COUNT_ON_PAGE = 5;
    public static final int DEFAULT_FIRST_PAGE_NUMBER = 1;
    public static final int DEFAULT_LAST_PAGE_NUMBER = -1;
    public static final int DEFAULT_ZERO_PAGE_NUMBER = 0;

    private Constants() {
    }
}

package it.academy.util.constants;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;

public final class Constants {

    public static final int COUNT_ON_PAGE_3 = 3;
    public static final int DEFAULT_COUNT_ON_PAGE_5 = 5;
    public static final int COUNT_ON_PAGE_10 = 10;

    public static final int ZERO_PAGE_NUMBER = 0;
    public static final int FIRST_PAGE_NUMBER = 1;
    public static final int DEFAULT_LAST_PAGE_NUMBER = -1;

    public static final long ZERO_LONG_VALUE = 0L;
    public static final int ZERO_INT_VALUE = 0;

    public static final int YEAR = 1900;
    public static final int MONTH = 1;
    public static final int DEFAULT_DAY_NUMBER = 1;

    public static final String DELIMITER_STRING = "-";
    public static final String BLANK_STRING = "";
    public static final String PER_CENT_STRING = "%";
    public static final String NULL_STRING = "null";

    public static final ProjectStatus DEFAULT_PROJECT_STATUS = ProjectStatus.IN_PROCESS;
    public static final ProposalStatus DEFAULT_PROPOSAL_STATUS = ProposalStatus.CONSIDERATION;
    public static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;

    private Constants() {
    }
}

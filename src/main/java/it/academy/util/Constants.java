package it.academy.util;

import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;

public final class Constants {

    public static final String SELECT_ALL_FROM_TABLE = "SELECT c FROM %s c";
    public static final String SELECT_COUNT_FROM_TABLE = "SELECT COUNT (c) FROM %s c";

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

    public static final String LOGIN_SERVLET = "login_servlet";
    public static final String LOGOUT_SERVLET = "logout_servlet";
    public static final String TOMAIN_SERVLET = "tomain_servlet";
    public static final String CREATE_CONTRACTOR_SERVLET = "create_contractor_servlet";
    public static final String MAIN_CONTRACTOR_SERVLET = "main_contractor_servlet";
    public static final String GET_MY_CHAPTERS_SERVLET = "get_my_chapters_servlet";
    public static final String GET_MY_PROPOSAL_SERVLET = "get_my_proposals_servlet";
    public static final String GET_MY_PROJECTS_BY_DEVELOPER_SERVLET = "get_my_projects_by_developer_servlet";
    public static final String GET_MY_CALCULATION_SERVLET = "get_my_calculation_servlet";
    public static final String CREATE_CALCULATION_SERVLET = "create_calculation_servlet";
    public static final String CREATE_DEVELOPER_SERVLET = "create_developer_servlet";
    public static final String MAIN_DEVELOPER_SERVLET = "main_developer_servlet";
    public static final String GET_CHAPTERS_OF_PROJECT_SERVLET = "get_chapters_of_project_servlet";
    public static final String CREATE_USER_SERVLET = "create_user_servlet";
    public static final String REDIRECTION_SERVLET = "redirection_servlet";


    public static final String EXCEPTION_PAGES_EXCEPTION_PAGE_1_JSP = "/exception_pages/exception_page_1.jsp";
    public static final String EXCEPTION_PAGES_EXCEPTION_CREATION_PAGE_2_JSP = "/exception_pages/exception_creation_page_2.jsp";
    public static final String EXCEPTION_PAGES_EXCEPTION_IN_WORK_PAGE_3_JSP = "/exception_pages/exception_in_work_page_3.jsp";

    public static final String INDEX_JSP = "/index.jsp";
    public static final String LOGIN_PAGE_JSP = "/login_page.jsp";
    public static final String CREATE_USER_PAGE_JSP = "/create_user_page.jsp";
    public static final String SELECT_NEW_USER_ROLE_PAGE_JSP = "/select_new_user_role_page.jsp";
    public static final String LIST_WITH_CHAPTER_NAMES_JSP = "/list_with_chapter_names.jsp";

    public static final String CONTRACTOR_PAGES_MAIN_JSP = "/contractor_pages/main.jsp";
    public static final String CONTRACTOR_PAGES_CREATE_CALCULATION_JSP = "/contractor_pages/create_calculation.jsp";
    public static final String CONTRACTOR_PAGES_LIST_WITH_CALCULATIONS_JSP = "/contractor_pages/list_with_calculations.jsp";
    public static final String CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP = "/contractor_pages/list_with_projects.jsp";
    public static final String CONTRACTOR_PAGES_LIST_WITH_CHAPTERS_JSP = "/contractor_pages/list_with_chapters.jsp";
    public static final String CONTRACTOR_PAGES_LIST_WITH_DEVELOPERS_JSP = "/contractor_pages/list_with_developers.jsp";
    public static final String CONTRACTOR_PAGES_LIST_WITH_PROPOSALS_JSP = "/contractor_pages/list_with_proposals.jsp";
    public static final String CONTRACTOR_PAGES_LIST_WITH_FREE_CHAPTERS_JSP = "/contractor_pages/list_with_free_chapters.jsp";


    public static final String DEVELOPER_PAGES_MAIN_JSP = "/developer_pages/main.jsp";
    public static final String DEVELOPER_PAGES_LIST_WITH_PROJECTS_JSP = "/developer_pages/list_with_projects.jsp";
    public static final String DEVELOPER_PAGES_LIST_WITH_CHAPTERS_JSP = "/developer_pages/list_with_chapters.jsp";

    public static final String ADMIN_PAGES_MAIN_JSP = "/admin_pages/main.jsp";


    public static final UserStatus DEFAULT_USER_STATUS = UserStatus.AKTIVE;
    public static final ProjectStatus DEFAULT_PROJECT_STATUS = ProjectStatus.IN_PROCESS;
    public static final ChapterStatus DEFAULT_CHAPTER_STATUS = ChapterStatus.OCCUPIED;
    public static final ProposalStatus DEFAULT_PROPOSAL_STATUS = ProposalStatus.CONSIDERATION;


    public static final String ID_PARAM = "id";
    public static final String USER_STATUS_PARAM = "user_status";
    public static final String TODO_PARAM = "todo";
    public static final String PROJECT_COUNT_ON_PAGE_PARAM = "project_count_on_page";
    public static final String DEVELOPER_COUNT_ON_PAGE_PARAM = "developer_count_on_page";
    public static final String PROJECT_PAGE_PARAM = "project_page";
    public static final String DEVELOPER_PAGE_PARAM = "developer_page";
    public static final String PROJECT_STATUS_PARAM = "project_status";
    public static final String PROJECT_DTO_LIST_PARAM = "project_dto_list";
    public static final String CHAPTER_ID_PARAM = "chapter_id";
    public static final String DEVELOPER_ID_PARAM = "developer_id";
    public static final String WORK_PRICE_PLAN_PARAM = "work_price_plan";
    public static final String MM_PARAM = "MM";
    public static final String YYYY_PARAM = "YYYY";
    public static final String MESSAGE_PARAM = "message";
    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";
    public static final String ROLE_PARAM = "role";
    public static final String NAME_PARAM = "name";
    public static final String CITY_PARAM = "city";
    public static final String STREET_PARAM = "street";
    public static final String BUILDING_PARAM = "building";
    public static final String CALCULATION_PAGE_PARAM = "calculation_page";
    public static final String CALCULATION_COUNT_ON_PAGE_PARAM = "calculation_count_on_page";
    public static final String CALCULATION_DTO_LIST_PARAM = "calculation_dto_list";
    public static final String DEVELOPER_DTO_LIST_PARAM = "developer_dto_list";
    public static final String CALCULATION_ID_PARAM = "calculation_id";
    public static final String WORK_PRICE_FACT_PARAM = "work_price_fact";
    public static final String PROJECT_ID_PARAM = "project_id";
    public static final String PROJECT_NAME_PARAM = "project_name";
    public static final String PROJECT_ADDRESS_PARAM = "project_address";
    public static final String DEVELOPER_ADDRESS_PARAM = "developer_address";
    public static final String PROJECT_DEVELOPER_PARAM = "project_developer";
    public static final String PROJECT_CONTRACTOR_PARAM = "project_contractor";
    public static final String CHAPTER_DTO_LIST_PARAM = "chapter_dto_list";
    public static final String CHAPTER_PAGE_PARAM = "chapter_page";
    public static final String CHAPTER_COUNT_ON_PAGE_PARAM = "chapter_count_on_page";
    public static final String CHAPTER_STATUS_PARAM = "chapter_status";
    public static final String CHAPTER_NAME_PARAM = "chapter_name";
    public static final String DEVELOPER_DEBT_PARAM = "developer_debt";
    public static final String DEVELOPER_NAME_PARAM = "developer_name";
    public static final String CHAPTER_PRICE_PARAM = "chapter_price";
    public static final String URL_PARAM = "url";
    public static final String SHOW_PROJECT_LIST_BY_DEVELOPER_PARAM = "show_project_list_by_developer";
    public static final String PROPOSAL_ID_PARAM = "proposal_id";
    public static final String PROPOSAL_PAGE_PARAM = "proposal_page";
    public static final String PROPOSAL_COUNT_ON_PAGE_PARAM = "proposal_count_on_page";
    public static final String PROPOSAL_STATUS_PARAM = "proposal_status";
    public static final String PROPOSAL_DTO_LIST_PARAM = "proposal_dto_list";
    public static final String NEW_PROPOSAL_STATUS_PARAM = "new_proposal_status";
    public static final String CHAPTER_NAMES_LIST_PARAM = "chapter_names_list";



    public static final String CALCULATION_NOT_CREATED = "Calculation not created!";
    public static final String ACCOUNT_NOT_CREATE = "Account not create!";
    public static final String EMAIL = "Email ";
    public static final String IS_OCCUPIED = " is occupied!";
    public static final String CALCULATION_NOT_UPDATED = "Calculation not updated!";
    public static final String INVALID_VALUE = "Invalid Value!";
    public static final String BAD_CONNECTION = "Bad connection...";
    public static final String GETTING_OF_DEBT = "Getting of debt ";
    public static final String WITH_DEVELOPER_ID = " with developerId ";
    public static final String FAILED = " failed.";
    public static final String GETING_OF_CONTRACTOR_DEBT_BY_CONTRACTOR_ID = "Geting of contractorDebt by contractorId ";
    public static final String AND_DEVELOPER_ID = " and developerId ";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL = "There is no such data in DB with email=";
    public static final String NOT_OCCUPIED = " not occupied";
    public static final String OCCUPIED = " occupied";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_ID = "There is no such data in DB with userId=";
    public static final String USER_CANCELED_USER_ID = "User canceled userId=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_PROJECT_STATUS = "There is no such data in DB with projectStatus=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS = "There is no such data in DB with userStatus=";
    public static final String USER_DELETE_ID = "User delete id=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID = "There is no such data in DB with id=";
    public static final String DELETE_FAIL_USER_ID = "delete fail user id=";
    public static final String FAILED_BY_CONSTRAINT = "Failed by constraint.";
    public static final String CALCULATION_DELETE_ID = "Calculation delete id=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID1 = "There is no such data in DB with id=";
    public static final String DELETE_FAIL_CALCULATION_ID = "delete fail calculation id=";
    public static final String CHAPTER_DELETE_ID = "Chapter delete id=";
    public static final String DELETE_FAIL_CHAPTER_ID = "delete failchapter id=";
    public static final String MONEY_TRANSFER_DELETE_ID = "MoneyTransfer delete id=";
    public static final String DELETE_FAIL_MONEY_TRANSFER_ID = "delete fail moneyTransfer id=";
    public static final String PROJECT_DELETE_ID = "Project delete id=";
    public static final String DELETE_FAIL_PROJECT_ID = "delete fail project id=";
    public static final String PROPOSAL_DELETE_ID = "Proposal delete id=";
    public static final String DELETE_FAIL_PROPOSAL_ID = "delete fail proposal id=";
    public static final String CREATED_SUCCESSFUL = "Created successful";
    public static final String CONTRACTOR_CREATED_ID = "Contractor created: id=";
    public static final String CONTRACTOR_NOT_FOUND_WITH_ID = "Contractor not found with id=";
    public static final String USER_NOT_CONTRACTOR_ID = "User not Contractor. id=";
    public static final String USER_NOT_CONTRACTOR = "User not Contractor";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID = "There is no such data in DB with contractorId=";
    public static final String AND_DELELOPER_ID = " and deleloperId=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB = "There is no such data in DB";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHPTER_NAME = "There is no such data in DB with chpterName=";
    public static final String AND_PROJECT_ID = " and projectId=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHAPTER_ID = "There is no such data in DB with chapterId=";
    public static final String WORKPRICE_UPDATED_ID = "Workprice updated. id=";
    public static final String VALUE = ", value=";
    public static final String WORKPRICE_NOT_UPDATED_ID = "Workprice not updated. id=";
    public static final String CHAPTER_NOT_FOUND_ID = "Chapter not found. id=";
    public static final String CALCULATION_CREATED_ID = "Calculation created. id=";
    public static final String CALCULATION_NOT_CREATED_CHAPTER_ID = "Calculation not created. chapterId=";
    public static final String PROPOSAL_NOT_FOUND_ID = "Proposal not found. id=";
    public static final String PROPOSAL_STATUS_UPDATE_TO = "Proposal status update to ";
    public static final String PROPOSAL_STATUS_NOT_UPDATE_ID = "Proposal status not update. id=";
    public static final String DATA_ALREDY_EXIST_IN_DB_ID = "Data alredy exist in DB. id=";
    public static final String THERE_IS_NO_SUCH_PROPOSAL_IN_DB_WITH_CHAPTER_ID = "There is no such proposal in DB with chapterId ";
    public static final String AND_CONTRACTOR_ID = " and contractorId ";
    public static final String CREATED_PROPOSAL_WITH_ID = "Created proposal with id=";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id=";
    public static final String USER_NOT_DEVELOPER_ID = "User not Developer. id=";
    public static final String USER_NOT_DEVELOPER = "User not Developer";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_DEVELOPER_ID = "There is no such data in DB with developerId=";
    public static final String DEVELOPER_NOT_FOUND_WITH_ID = "Developer not found with id=";
    public static final String PROJECT_NOT_FOUND_WITH_ID = "Project not found with id=";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_WITH_PROJECT_ID = "There is no such data in DB with projectId=";
    public static final String CHAPTER_READ_FROM_DB = "Chapter read from DB";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID = "There is no such data in DB chapterId=";
    public static final String CHAPTER_STATUS_CHANGED = "Chapter status changed ";
    public static final String PROPOSAL_READ_FROM_DB = "Proposal read from DB ";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_PROPOSAL_ID = "There is no such data in DB proposalId=";
    public static final String PROPOSAL_STATUS_CHANGED = "Proposal status changed ";
    public static final String THERE_IS_NO_SUCH_DATA_IN_DB_PROJECT_ID = "There is no such data in DB projectId=";
    public static final String PROJECT_STATUS_CHANGED = "Project status changed ";
    public static final String ROLE_IS_INVALID = "Role is invalid!";
    public static final String EXIT_FROM = "Exit from ";
    public static final String ACCOUNT = " account";
    public static final String USER_HAS_NOT_ACTIVE_STATUS_IT_IS_IMPOSSIBLE_TO_USE_THIS_ACCOUNT = "User has not active status. It is impossible to use this account!";
    public static final String PASSWORD_IS_INVALID = "Password is invalid!";
    public static final String PASSWORD_IS_EMPTY = "Password is empty!";
    public static final String EMAIL_IS_INVALID = "Email is invalid!";
    public static final String EMAIL_IS_EMPTY = "Email is empty!";
    public static final String BLANK_STRING = "";
    public static final String SLASH_STRING = "/";
    public static final String CALCULATION_NOT_FOUND_WITH_ID = "Calculation not found with id=";
    public static final String PROPOSAL_NOT_CREATE = "Proposal not create.";


    private Constants() {
    }
}

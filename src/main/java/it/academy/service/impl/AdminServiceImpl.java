package it.academy.service.impl;

import it.academy.converters.*;
import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.pojo.*;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.AdminService;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;
import static it.academy.util.constants.Constants.PER_CENT_STRING;
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
public class AdminServiceImpl implements AdminService {

    private final ContractorDao contractorDao = new ContractorDaoImpl();
    private final DeveloperDao developerDao = new DeveloperDaoImpl();
    private final CalculationDao calculationDao = new CalculationDaoImpl();
    private final ChapterDao chapterDao = new ChapterDaoImpl();
    private final MoneyTransferDao moneyTransferDao = new MoneyTransferDaoImpl();
    private final ProjectDao projectDao = new ProjectDaoImpl();
    private final ProposalDao proposalDao = new ProposalDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    private static AdminServiceImpl instance;

    private AdminServiceImpl() {
    }

    public static synchronized AdminServiceImpl getInstance() {

        if (instance == null) {
            instance = new AdminServiceImpl();
        }
        return instance;
    }

    @Override
    public LoginDto logIn(UserDto userDtoFromUi) {

        String exceptionMessage = null;
        UserDto userFromDb = null;
        String url = null;

        String email = userDtoFromUi.getEmail();
        if (email != null && !email.isBlank()) {
            User user = null;
            try {
                user = userDao.executeInOneEntityTransaction(() -> {
                    User user1 = null;
                    try {
                        user1 = userDao.getUser(email);
                    } catch (NoResultException e) {
                        log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL + email, e);
                    }
                    return user1;
                });
            } catch (Exception e) {
                exceptionMessage = SOMETHING_WENT_WRONG;
            }
            if (user != null) {
                String password = userDtoFromUi.getPassword();
                if (password != null && !password.isEmpty()) {
                    if (password.equals(user.getPassword())) {
                        if (UserStatus.ACTIVE.equals(user.getStatus())) {
                            Roles role = user.getRole();
                            userFromDb = UserConverter.convertToDto(user);
                            switch (role) {
                                case CONTRACTOR:
                                    url = CONTRACTOR_PAGES_MAIN_JSP;
                                    break;
                                case DEVELOPER:
                                    url = DEVELOPER_PAGES_MAIN_JSP;
                                    break;
                                case ADMIN:
                                    url = ADMIN_PAGES_MAIN_JSP;
                                    break;
                                default:
                                    exceptionMessage = ROLE_IS_INVALID;
                            }
                        } else {
                            exceptionMessage = USER_HAS_NOT_ACTIVE_STATUS_IT_IS_IMPOSSIBLE_TO_USE_THIS_ACCOUNT;
                        }
                    } else {
                        exceptionMessage = PASSWORD_IS_INVALID;
                    }
                } else {
                    exceptionMessage = PASSWORD_IS_EMPTY;
                }
            } else {
                exceptionMessage = EMAIL_IS_INVALID;
            }
        } else {
            exceptionMessage = EMAIL_IS_EMPTY;
        }

        LoginDto loginDto = LoginDto.builder().build();
        if (exceptionMessage != null) {
            loginDto.setExceptionMessage(exceptionMessage);
        } else {
            loginDto.setUrl(url);
            loginDto.setUserFromDb(userFromDb);
        }
        return loginDto;
    }

    @Override
    public LoginDto createAdmin(CreateRequestDto dto) {

        String exceptionMessage = null;

        try {
            User userCreated = userDao.executeInOneEntityTransaction(() -> {
                User user = null;
                try {
                    user = userDao.getUser(dto.getEmail());
                } catch (NoResultException e) {
                    log.trace(EMAIL + dto.getEmail() + NOT_OCCUPIED, e);
                }
                if (user != null) {
                    log.trace(EMAIL + dto.getEmail() + OCCUPIED);
                    throw new EmailOccupaidException(EMAIL + IS_OCCUPIED);
                } else {
                    User newUser = User.builder()
                                       .email(dto.getEmail())
                                       .password(dto.getPassword())
                                       .role(Roles.ADMIN)
                                       .status(UserStatus.ACTIVE)
                                       .build();
                    userDao.create(newUser);
                    return newUser;
                }
            });
            if (userCreated == null) {
                throw new NotCreateDataInDbException();
            }
            log.trace(ACCOUNT + CREATED_SUCCESSFUL + userCreated.getId());
        } catch (EmailOccupaidException e) {
            exceptionMessage = EMAIL + IS_OCCUPIED;
            log.debug(EMAIL + IS_OCCUPIED, e);
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = ACCOUNT_NOT_CREATE;
            log.debug(ACCOUNT_NOT_CREATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }

        LoginDto loginDto;
        if (exceptionMessage != null) {
            loginDto = LoginDto.builder()
                           .exceptionMessage(exceptionMessage)
                           .build();
        } else {
            loginDto = LoginDto.builder()
                           .url(SLASH_STRING + GET_ALL_ADMINS_ADMINISTRATOR_SERVLET)
                           .build();
        }
        return loginDto;
    }

    @Override
    public DtoWithPageForUi<UserDto> createUser(String role) {

        String exceptionMessage = null;
        Roles roles = null;
        if (Roles.CONTRACTOR.toString().equalsIgnoreCase(role)) {
            roles = Roles.CONTRACTOR;
        } else if (Roles.DEVELOPER.toString().equalsIgnoreCase(role)) {
            roles = Roles.DEVELOPER;
        } else {
            exceptionMessage = ROLE_IS_INVALID;
        }
        return new DtoWithPageForUi<>(null, null, null, roles,
            exceptionMessage, null, null, null, CREATE_USER_PAGE_JSP, null);
    }

    @Override
    public UserDto getUser(String email) throws Exception {

        User userFromDb = userDao.executeInOneEntityTransaction(() -> {
            User user = null;
            try {
                user = userDao.getUser(email);
            } catch (NoResultException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL + email, e);
            } catch (Exception e) {
                log.error(SOMETHING_WENT_WRONG, e);
            }
            return user;
        });
        return UserConverter.convertToDto(userFromDb);
    }

    @Override
    public DtoWithPageForUi<ContractorDto> getAllContractors(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ContractorDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        String search = PER_CENT_STRING + dto.getSearch() + PER_CENT_STRING;
        UserStatus status = null;
        try {
            UserStatus userStatus = (UserStatus) dto.getStatus();
            Page<Contractor> contractorPage;
            contractorPage = contractorDao.executeInOnePageTransaction(() -> {
                Page<Contractor> page1 = null;
                try {
                    long totalCount = contractorDao.getCountOfContractors(userStatus, search);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(contractorDao.getContractors(userStatus, search, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + userStatus, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            status = userStatus;
            page = contractorPage.getPageNumber();
            lastPageNumber = contractorPage.getLastPageNumber();
            list.addAll(contractorPage.getList()
                            .stream()
                            .map(contractor -> ContractorConverter.convertToDto(contractor, null))
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, status,
            exceptionMessage, null, null, list, ADMIN_PAGES_LIST_WITH_CONTRACTORS_JSP, dto.getSearch());
    }

    @Override
    public DtoWithPageForUi<UserDto> toMainPage(Object role) {

        Roles roles = null;
        String exceptionMessage = null;
        String url = null;
        try {
            roles = (Roles) role;
        } catch (ClassCastException e) {
            exceptionMessage = ROLE_IS_INVALID;
            log.debug(ROLE_IS_INVALID, e);
        }

        switch (roles) {
            case DEVELOPER:
                url = DEVELOPER_PAGES_MAIN_JSP;
                break;
            case CONTRACTOR:
                url = CONTRACTOR_PAGES_MAIN_JSP;
                break;
            case ADMIN:
                url = ADMIN_PAGES_MAIN_JSP;
                break;
            default:
                exceptionMessage = ROLE_IS_INVALID;
                log.debug(ROLE_IS_INVALID + roles);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, url, null);
    }

    @Override
    public DtoWithPageForUi<UserDto> getAllAdministrators() {

        String exceptionMessage = null;
        List<UserDto> list = new ArrayList<>();
        try {
            list.addAll(userDao.executeInOneListTransaction(userDao::getAdministrators)
                            .stream()
                            .map(UserConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (NoResultException e) {
            log.trace(THERE_IS_NO_SUCH_DATA_IN_DB, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, list, ADMIN_PAGES_LIST_WITH_ADMINS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<DeveloperDto> getAllDevelopers(FilterPageDto dto) {

        String exceptionMessage = null;
        List<DeveloperDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        String search = PER_CENT_STRING + dto.getSearch() + PER_CENT_STRING;
        UserStatus status = null;
        try {
            UserStatus userStatus = (UserStatus) dto.getStatus();
            Page<Developer> developerPage;
            developerPage = developerDao.executeInOnePageTransaction(() -> {
                Page<Developer> page1 = null;
                try {
                    long totalCount = developerDao.getCountOfDevelopers(userStatus, search);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(developerDao.getDevelopers(userStatus, search, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + userStatus, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            status = userStatus;
            page = developerPage.getPageNumber();
            lastPageNumber = developerPage.getLastPageNumber();
            list.addAll(developerPage.getList()
                            .stream()
                            .map(developer -> DeveloperConverter.convertToDto(developer, null))
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, status,
            exceptionMessage, null, null, list, ADMIN_PAGES_LIST_WITH_DEVELOPERS_JSP, dto.getSearch());
    }

    @Override
    public DtoWithPageForUi<ProjectDto> getProjectsByDeveloper(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        ProjectStatus status = null;
        Integer lastPageNumber = FIRST_PAGE_NUMBER;

        try {
            ProjectStatus projectStatus = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = projectDao.executeInOnePageTransaction(() -> {
                Page<Project> page1 = null;
                try {
                    long totalCount = projectDao.getCountOfProjectsByDeveloperId(dto.getId(), projectStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.setMap(projectDao.getProjectsByDeveloperId(
                        dto.getId(), projectStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });

            status = projectStatus;
            page = projectPage.getPageNumber();
            lastPageNumber = projectPage.getLastPageNumber();
            Map<Project, Integer[]> map = projectPage.getMap();
            list.addAll(map.keySet().stream()
                            .map(project -> {
                                Integer[] values = map.get(project);
                                return ProjectConverter.convertToDto(project, values[0], values[1] - values[2]);
                            })
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, status,
            exceptionMessage, dto.getId(), dto.getName(), list, ADMIN_PAGES_LIST_WITH_PROJECTS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByProjectId(long projectId) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            list.addAll(chapterDao.executeInOneListTransaction(
                () -> new ArrayList<>(chapterDao.getChaptersByProjectId(projectId).keySet()))
                            .stream()
                            .map(chapter -> ChapterConverter.convertToDto(chapter, null))
                            .collect(Collectors.toList()));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, projectId, null, list, ADMIN_PAGES_LIST_WITH_CHAPTERS_FROM_PROJECT_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByContractorId(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;

        try {
            Page<Chapter> chapterPage = chapterDao.executeInOnePageTransaction(() -> {
                Page<Chapter> page1 = null;
                try {
                    long totalCount = chapterDao.getCountOfChaptersByContractorId(dto.getId());
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(chapterDao.getChaptersByContractorId(
                        dto.getId(), page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });


            page = chapterPage.getPageNumber();
            lastPageNumber = chapterPage.getLastPageNumber();
            list.addAll(chapterPage.getList()
                            .stream()
                            .map(chapter -> ChapterConverter.convertToDto(chapter, null))
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, null,
            exceptionMessage, dto.getId(), dto.getName(), list, ADMIN_PAGES_LIST_WITH_CHAPTERS_FROM_CONTRACTOR_JSP, null);
    }

    @Override
    public DtoWithPageForUi<MoneyTransferDto> getMoneyTransfers(long calculationId) {

        String exceptionMessage = null;
        List<MoneyTransferDto> list = new ArrayList<>();

        try {
            list.addAll(moneyTransferDao.executeInOneListTransaction(
                () -> moneyTransferDao.getMoneyTransfersByCalculationId(calculationId)).stream()
                            .map(MoneyTransferConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + calculationId, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, calculationId, null, list, ADMIN_PAGES_LIST_WITH_MONEY_TRANSFERS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(FilterPageDto dto) {

        String exceptionMessage = null;
        List<CalculationDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;

        try {
            Page<Calculation> calculationPage = calculationDao.executeInOnePageTransaction(() -> {
                Page<Calculation> page1 = null;
                try {
                    long totalCount = calculationDao.getCountOfCalculationsByChapterId(dto.getId());
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.setMap(calculationDao.getCalculationsByChapterId(
                        dto.getId(), page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.trace(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            page = calculationPage.getPageNumber();
            lastPageNumber = calculationPage.getLastPageNumber();
            Map<Calculation, Integer[]> map = calculationPage.getMap();
            list.addAll(map.keySet().stream()
                            .map(calculation -> {
                                Integer[] values = map.get(calculation);
                                return CalculationConverter.convertToDto(calculation, values[0], values[1]);
                            })
                            .collect(Collectors.toList()));

        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, null,
            exceptionMessage, dto.getId(), dto.getName(), list, ADMIN_PAGES_LIST_WITH_CALCULATIONS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getProposalsByChapterId(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProposalStatus status = null;

        try {
            ProposalStatus proposalStatus = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage;
            proposalPage = proposalDao.executeInOnePageTransaction(() -> {
                Page<Proposal> page1 = null;
                try {
                    long totalCount = proposalDao.getCountOfProposalsByChapterId(dto.getId(), proposalStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(proposalDao.getProposalsByChapterId(
                        dto.getId(), proposalStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + proposalStatus, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            status = proposalStatus;
            page = proposalPage.getPageNumber();
            lastPageNumber = proposalPage.getLastPageNumber();
            list.addAll(proposalPage.getList().stream()
                            .map(ProposalConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, status,
            exceptionMessage, dto.getId(), dto.getName(), list, ADMIN_PAGES_LIST_WITH_PROPOSALS_FROM_CHAPTER_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getProposalsByContractorId(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProposalStatus status = null;

        try {
            ProposalStatus proposalStatus = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage;
            proposalPage = proposalDao.executeInOnePageTransaction(() -> {
                Page<Proposal> page1 = null;
                try {
                    long totalCount = proposalDao.getCountOfProposalsByContractorId(dto.getId(), proposalStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(proposalDao.getProposalsByContractorId(
                        dto.getId(), proposalStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + proposalStatus, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            status = proposalStatus;
            page = proposalPage.getPageNumber();
            lastPageNumber = proposalPage.getLastPageNumber();
            list.addAll(proposalPage.getList().stream()
                            .map(ProposalConverter::convertToDto)
                            .collect(Collectors.toList()));
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.error(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(page, count, lastPageNumber, status,
            exceptionMessage, dto.getId(), null, list, ADMIN_PAGES_LIST_WITH_PROPOSALS_FROM_CONTRACTOR_JSP, null);
    }

    @Override
    public DtoWithPageForUi<UserDto> changeUserStatus(ChangeRequestDto dto) {

        String exceptionMessage = null;
        Roles role = null;
        String url = null;

        try {
            role = Roles.valueOf(dto.getName());
        } catch (IllegalArgumentException e) {
            exceptionMessage = ROLE_IS_INVALID;
            log.debug(INVALID_VALUE + dto.getName(), e);
        }
        if (role != null) {
            switch (role) {
                case CONTRACTOR:
                    url = GET_ALL_CONTRACTORS_ADMINISTRATOR_SERVLET;
                    break;
                case DEVELOPER:
                    url = GET_ALL_DEVELOPERS_ADMINISTRATOR_SERVLET;
                    break;
                case ADMIN:
                    url = GET_ALL_ADMINS_ADMINISTRATOR_SERVLET;
                    break;
                default:
                    exceptionMessage = ROLE_IS_INVALID;
                    log.debug(INVALID_VALUE + dto.getName());
            }

            UserStatus newStatus = (UserStatus) dto.getStatus();
            boolean isUpdated = false;
            try {
                isUpdated = userDao.executeInOneBoolTransaction(() -> {
                    User user = null;
                    try {
                        user = userDao.get(dto.getId());
                    } catch (EntityNotFoundException e) {
                        log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_ID + dto.getId());
                    }
                    if (user != null) {
                        user.setStatus(newStatus);
                        userDao.update(user);
                        log.trace(USER_STATUS_CHANED_USER_ID + dto.getId());
                        return true;
                    }
                    return false;
                });
            } catch (ClassCastException e) {
                exceptionMessage = INVALID_VALUE;
                log.debug(INVALID_VALUE + dto.getStatus().toString(), e);
            } catch (IOException e) {
                exceptionMessage = BAD_CONNECTION;
                log.error(BAD_CONNECTION, e);
            } catch (Exception e) {
                exceptionMessage = SOMETHING_WENT_WRONG;
                log.error(SOMETHING_WENT_WRONG, e);
            }
            if (!isUpdated) {
                exceptionMessage = PROJECT_STATUS_NOT_UPDATE;
            }
        } else {
            exceptionMessage = ROLE_IS_INVALID;
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + url, null);
    }

    @Override
    public DtoWithPageForUi<UserDto> deleteUser(ChangeRequestDto dto) {

        String exceptionMessage = null;
        Roles role = null;
        String url = null;

        try {
            role = Roles.valueOf(dto.getName());
        } catch (IllegalArgumentException e) {
            exceptionMessage = ROLE_IS_INVALID;
            log.debug(INVALID_VALUE + dto.getName(), e);
        }
        if (role != null) {
            switch (role) {
                case CONTRACTOR:
                    url = GET_ALL_CONTRACTORS_ADMINISTRATOR_SERVLET;
                    break;
                case DEVELOPER:
                    url = GET_ALL_DEVELOPERS_ADMINISTRATOR_SERVLET;
                    break;
                case ADMIN:
                    url = GET_ALL_ADMINS_ADMINISTRATOR_SERVLET;
                    break;
                default:
                    exceptionMessage = ROLE_IS_INVALID;
                    log.debug(INVALID_VALUE + dto.getName());
            }
            try {
                userDao.executeInOneVoidTransaction(() -> {
                    try {
                        User user = userDao.get(dto.getId());
                        Roles role1 = user.getRole();
                        switch (role1) {
                            case CONTRACTOR:
                                contractorDao.executeInOneVoidTransaction(() -> {
                                    contractorDao.delete(dto.getId());
                                    userDao.delete(dto.getId());
                                });
                                break;
                            case DEVELOPER:
                                developerDao.executeInOneVoidTransaction(() -> {
                                    developerDao.delete(dto.getId());
                                    userDao.delete(dto.getId());
                                });
                                break;
                            case ADMIN:
                                userDao.delete(dto.getId());
                                break;
                        }
                        log.trace(USER_DELETE_ID + dto.getId());
                    } catch (EntityNotFoundException e) {
                        log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + dto.getId(), e);
                    }
                });
            } catch (ConstraintViolationException e) {
                exceptionMessage = FAILED_BY_CONSTRAINT;
                log.debug(DELETE_FAIL_USER_ID + dto.getId(), e);
            } catch (IOException e) {
                exceptionMessage = BAD_CONNECTION;
                log.error(BAD_CONNECTION, e);
            } catch (Exception e) {
                exceptionMessage = SOMETHING_WENT_WRONG;
                log.error(SOMETHING_WENT_WRONG, e);
            }
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + url, null);
    }

    @Override
    public DtoWithPageForUi<CalculationDto> deleteCalculation(long calculationId) {

        String exceptionMessage = null;
        try {
            calculationDao.executeInOneVoidTransaction(() -> {
                try {
                    calculationDao.delete(calculationId);
                    log.trace(CHAPTER_DELETE_ID + calculationId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID1 + calculationId, e);
                }
            });
        } catch (ConstraintViolationException e) {
            exceptionMessage = FAILED_BY_CONSTRAINT;
            log.error(DELETE_FAIL_CALCULATION_ID + calculationId, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_CALCULATION_ADMINISTRATOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> deleteChapter(long chapterId) {

        String exceptionMessage = null;
        try {
            chapterDao.executeInOneVoidTransaction(() -> {
                try {
                    chapterDao.delete(chapterId);
                    log.trace(CHAPTER_DELETE_ID + chapterId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + chapterId, e);
                }
            });
        } catch (ConstraintViolationException e) {
            exceptionMessage = FAILED_BY_CONSTRAINT;
            log.error(DELETE_FAIL_CHAPTER_ID + chapterId, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_CHAPTERS_FROM_PROJECT_ADMINISTRATOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ProjectDto> deleteMoneyTransfer(long moneyTransferId) {

        String exceptionMessage = null;
        try {
            moneyTransferDao.executeInOneVoidTransaction(() -> {
                try {
                    moneyTransferDao.delete(moneyTransferId);
                    log.trace(MONEY_TRANSFER_DELETE_ID + moneyTransferId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + moneyTransferId, e);
                }
            });
        } catch (ConstraintViolationException e) {
            exceptionMessage = FAILED_BY_CONSTRAINT;
            log.error(DELETE_FAIL_MONEY_TRANSFER_ID + moneyTransferId, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_MONEY_TRANSFER_ADMINISTRATOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ProjectDto> deleteProject(long projectId) {

        String exceptionMessage = null;
        try {

            projectDao.executeInOneVoidTransaction(() -> {
                try {
                    projectDao.delete(projectId);
                    log.trace(PROJECT_DELETE_ID + projectId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + projectId, e);
                }
            });
        } catch (ConstraintViolationException e) {
            exceptionMessage = FAILED_BY_CONSTRAINT;
            log.debug(DELETE_FAIL_PROJECT_ID + projectId, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_PROJECTS_ADMINISTRATOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> deleteProposal(ChangeRequestDto dto) {

        String exceptionMessage = null;
        boolean showByContractor = Boolean.parseBoolean(dto.getName());
        String url = showByContractor ?
                         SLASH_STRING + GET_PROPOSALS_FROM_CONTRACTOR_ADMINISTRATOR_SERVLET
                         : SLASH_STRING + GET_PROPOSALS_FROM_CHAPTER_ADMINISTRATOR_SERVLET;

        try {
            proposalDao.executeInOneVoidTransaction(() -> {
                try {
                    proposalDao.delete(dto.getId());
                    log.trace(PROPOSAL_DELETE_ID + dto.getId());
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + dto.getId(), e);
                }
            });
        } catch (ConstraintViolationException e) {
            exceptionMessage = FAILED_BY_CONSTRAINT;
            log.debug(DELETE_FAIL_PROPOSAL_ID + dto.getId(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG + dto.getId(), e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, url, null);
    }
}
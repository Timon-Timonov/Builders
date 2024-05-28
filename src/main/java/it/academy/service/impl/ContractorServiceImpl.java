package it.academy.service.impl;

import it.academy.converters.*;
import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.dto.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.*;
import it.academy.pojo.enums.*;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.ContractorService;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
public class ContractorServiceImpl implements ContractorService {

    private final ContractorDao contractorDao = new ContractorDaoImpl();
    private final DeveloperDao developerDao = new DeveloperDaoImpl();
    private final CalculationDao calculationDao = new CalculationDaoImpl();
    private final ChapterDao chapterDao = new ChapterDaoImpl();
    private final ProjectDao projectDao = new ProjectDaoImpl();
    private final ProposalDao proposalDao = new ProposalDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    private static ContractorServiceImpl instance;

    private ContractorServiceImpl() {
    }

    public static synchronized ContractorServiceImpl getInstance() {

        if (instance == null) {
            instance = new ContractorServiceImpl();
        }
        return instance;
    }

    @Override
    public LoginDto createContractor(CreateRequestDto dto) {

        String exceptionMessage = null;
        UserDto userFromDb = null;

        try {
            Contractor createdContractor = contractorDao.executeInOneEntityTransaction(() -> {
                Contractor contractor;
                User userFromDB = null;
                try {
                    userFromDB = userDao.executeInOneEntityTransaction(() -> {
                        User newUser;
                        try {
                            newUser = User.builder()
                                          .email(dto.getEmail())
                                          .password(dto.getPassword())
                                          .role(Roles.CONTRACTOR)
                                          .status(UserStatus.ACTIVE)
                                          .build();
                            userDao.create(newUser);
                        } catch (ConstraintViolationException e) {
                            log.error(EMAIL + dto.getEmail() + IS_OCCUPIED, e);
                            return null;
                        }
                        return newUser;
                    });
                } catch (RollbackException e) {
                    log.error(e);
                }

                if (userFromDB != null) {
                    Contractor newContractor = Contractor.builder()
                                                   .name(dto.getName())
                                                   .address(Address.builder()
                                                                .city(dto.getCity())
                                                                .street(dto.getStreet())
                                                                .building(dto.getBuilding())
                                                                .build())
                                                   .user(userFromDB)
                                                   .build();
                    contractorDao.create(newContractor);
                    log.trace(CONTRACTOR_CREATED_ID + newContractor.getId());
                    contractor = newContractor;
                } else {
                    throw new EmailOccupaidException(EMAIL + dto.getEmail() + OCCUPIED);
                }
                if (contractor.getId() == null) {
                    throw new NotCreateDataInDbException();
                }
                return contractor;
            });

            if (createdContractor == null) {
                throw new NotCreateDataInDbException();
            }
            userFromDb = UserConverter.convertToDto(createdContractor.getUser());
            log.trace(ACCOUNT + CREATED_SUCCESSFUL + createdContractor.getId());
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
                           .userFromDb(userFromDb)
                           .url(CONTRACTOR_PAGES_MAIN_JSP)
                           .build();
        }
        return loginDto;
    }

    @Override
    public DtoWithPageForUi<ProjectDto> getMyProjects(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;
        try {
            ProjectStatus projectStatus = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = projectDao.executeInOnePageTransaction(() -> {
                Page<Project> page1 = null;
                try {
                    long totalCount = projectDao.getCountOfProjectsByContractorId(dto.getId(), projectStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);

                    Map<Project, Integer[]> map = projectDao.getProjectsByContractorId(
                        dto.getId(), projectStatus, page1.getPageNumber(), count);

                    page1.setMap(map);
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + dto.getId());
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });

            status = projectStatus;
            page = projectPage.getPageNumber();
            lastPageNumber = projectPage.getLastPageNumber();
            Map<Project, Integer[]> projectMap = projectPage.getMap();

            list.addAll(projectMap.keySet()
                            .stream()
                            .map(project -> {
                                Integer[] values = projectMap.get(project);
                                Integer projectDebt = values[1] - values[2];
                                return ProjectConverter.convertToDto(project, values[0], projectDebt);
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
            exceptionMessage, null, null, list, CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ProjectDto> getMyProjectsByDeveloper(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;
        try {
            ProjectStatus projectStatus = (ProjectStatus) dto.getStatus();
            Page<Project> projectPage = projectDao.executeInOnePageTransaction(() -> {
                Page<Project> page1 = null;
                try {
                    long totalCount = projectDao.getCountOfProjectsByDeveloperIdContractorId(
                        dto.getId(), dto.getSecondId(), projectStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.setMap(projectDao.getProjectsByDeveloperIdContractorId(
                        dto.getId(), dto.getSecondId(), projectStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + dto.getSecondId() +
                                  AND_DELELOPER_ID + dto.getId());
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
                                Integer[] arr = map.get(project);
                                int price = arr[0];
                                int debt = arr[1] - arr[2];
                                return ProjectConverter.convertToDto(project, price, debt);
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
            exceptionMessage, dto.getId(), dto.getName(), list, CONTRACTOR_PAGES_LIST_WITH_PROJECTS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getAllChapterNames() {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();
        try {

            list.addAll(chapterDao.getAllChapterNames().stream()
                            .map(name -> ChapterDto.builder().chapterName(name).build())
                            .collect(Collectors.toList()));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        } finally {
            chapterDao.closeManager();
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, list, LIST_WITH_CHAPTER_NAMES_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getFreeChapters(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProjectStatus status = null;

        try {
            ProjectStatus projectStatus = (ProjectStatus) dto.getStatus();
            Page<Chapter> chapterPage = chapterDao.executeInOnePageTransaction(() -> {
                Page<Chapter> page1 = null;
                try {
                    long totalCount = chapterDao.getCountOfFreeChaptersByName(
                        dto.getId(), dto.getName(), projectStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(chapterDao.getFreeChapters(
                        dto.getId(), dto.getName(), projectStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });

            status = projectStatus;
            page = chapterPage.getPageNumber();
            lastPageNumber = chapterPage.getLastPageNumber();
            list.addAll(chapterPage.getList()
                            .stream()
                            .map(chapter -> ChapterConverter.convertToDto(chapter, null))
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
            exceptionMessage, null, dto.getName(), list, CONTRACTOR_PAGES_LIST_WITH_FREE_CHAPTERS_JSP, null);
    }


    @Override
    public DtoWithPageForUi<DeveloperDto> getMyDevelopers(FilterPageDto dto) {

        String exceptionMessage = null;
        List<DeveloperDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        String search = PER_CENT_STRING + dto.getSearch() + PER_CENT_STRING;
        ProjectStatus status = null;
        try {
            ProjectStatus projectStatus = (ProjectStatus) dto.getStatus();
            Page<Developer> developerPage = developerDao.executeInOnePageTransaction(() -> {
                Page<Developer> page1 = null;
                try {
                    long totalCount = developerDao.getCountOfDevelopers(dto.getId(), projectStatus, search);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);

                    Map<Developer, Integer[]> map = developerDao.getDevelopersForContractor(
                        dto.getId(), projectStatus, search, page1.getPageNumber(), count);

                    page1.setMap(map);
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });

            status = projectStatus;
            page = developerPage.getPageNumber();
            lastPageNumber = developerPage.getLastPageNumber();
            Map<Developer, Integer[]> developerMap = developerPage.getMap();

            list.addAll(developerMap.keySet()
                            .stream()
                            .map(developer -> {
                                Integer[] values = developerMap.get(developer);
                                Integer developerDebt = values[0] - values[1];
                                return DeveloperConverter.convertToDto(developer, developerDebt);
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
            exceptionMessage, null, null, list, CONTRACTOR_PAGES_LIST_WITH_DEVELOPERS_JSP, dto.getSearch());
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getMyProposals(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ProposalDto> list = new ArrayList<>();
        Integer page = FIRST_PAGE_NUMBER;
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        ProposalStatus status = null;
        try {
            ProposalStatus proposalStatus = (ProposalStatus) dto.getStatus();
            Page<Proposal> proposalPage = proposalDao.executeInOnePageTransaction(() -> {
                Page<Proposal> page1 = null;
                try {
                    long totalCount = proposalDao.getCountOfProposalsByContractorId(dto.getId(), proposalStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);


                    page1.getList().addAll(proposalDao.getProposalsByContractorId(
                        dto.getId(), proposalStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });

            status = proposalStatus;
            page = proposalPage.getPageNumber();
            lastPageNumber = proposalPage.getLastPageNumber();
            list.addAll(proposalPage.getList()
                            .stream()
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
            exceptionMessage, null, null, list, CONTRACTOR_PAGES_LIST_WITH_PROPOSALS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getMyChaptersByProjectId(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            Page<Chapter> page;
            page = chapterDao.executeInOnePageTransaction(() -> {
                Page<Chapter> page1 = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
                try {
                    page1.setMap(chapterDao.getChaptersByProjectIdContractorId(dto.getId(), dto.getSecondId()));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1;
            });
            Map<Chapter, Integer[]> map = page.getMap();
            list.addAll(map.keySet().stream()
                            .map(chapter -> {
                                Integer[] values = map.get(chapter);
                                return ChapterConverter.convertToDto(chapter, values[0] - values[1]);
                            })
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, dto.getId(), dto.getName(), list, CONTRACTOR_PAGES_LIST_WITH_CHAPTERS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<CalculationDto> getCalculationsByChapter(FilterPageDto dto) {

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
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
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
            exceptionMessage, dto.getId(), dto.getName(), list, CONTRACTOR_PAGES_LIST_WITH_CALCULATIONS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<CalculationDto> updateWorkPriceFact(ChangeRequestDto dto) {

        String exceptionMessage = null;
        try {
            boolean isUpdated = calculationDao.executeInOneBoolTransaction(() -> {
                int count = calculationDao.updateWorkPriceFact(dto.getCount(), dto.getId());
                if (1 == count) {
                    return true;
                } else {
                    log.debug(WORKPRICE_NOT_UPDATED_ID + dto.getId());
                    return false;
                }
            });
            if (!isUpdated) {
                throw new NotUpdateDataInDbException();
            }
            log.trace(WORKPRICE_UPDATED_ID + dto.getId() + VALUE + dto.getCount());
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = CALCULATION_NOT_UPDATED;
            log.debug(CALCULATION_NOT_UPDATED + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_MY_CALCULATION_CONTRACTOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<CalculationDto> createCalculation(CreateRequestDto dto) {

        String exceptionMessage = null;
        try {
            Calculation calculation = calculationDao.executeInOneEntityTransaction(() -> {
                Chapter chapter;
                try {
                    chapter = chapterDao.getChapterForCalculation(dto.getId());
                } catch (NoResultException e) {
                    log.error(CHAPTER_NOT_FOUND_ID + dto.getId(), e);
                    return null;
                } finally {
                    chapterDao.closeManager();
                }
                if (chapter != null) {
                    Calculation newCalculation = Calculation.builder()
                                                     .chapter(chapter)
                                                     .month(Date.valueOf(BLANK_STRING + dto.getInt1() + DELIMITER_STRING + dto.getInt2() + DELIMITER_STRING + DEFAULT_DAY_NUMBER))
                                                     .workPricePlan(dto.getInt3())
                                                     .build();
                    calculationDao.create(newCalculation);
                    return newCalculation;
                }
                log.error(CALCULATION_NOT_CREATED);
                return null;
            });
            if (calculation == null) {
                throw new NotCreateDataInDbException();
            }
            log.trace(CALCULATION_CREATED_ID + calculation.getId());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = CALCULATION_NOT_CREATED;
            log.debug(CALCULATION_NOT_CREATED + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_MY_CALCULATION_CONTRACTOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> setProposalStatus(ChangeRequestDto dto) {

        String exceptionMessage = null;

        try {
            ProposalStatus newStatus = (ProposalStatus) dto.getStatus();
            if (newStatus == null) {
                log.error(PROPOSAL_STATUS_NOT_UPDATE_ID + dto.getId() + NEW_STATUS + newStatus);
                throw new NotUpdateDataInDbException();
            }

            boolean isUpdated = proposalDao.executeInOneBoolTransaction(() -> {
                Proposal proposal;
                try {
                    proposal = proposalDao.get(dto.getId());
                } catch (EntityNotFoundException e) {
                    log.error(PROPOSAL_NOT_FOUND_ID + dto.getId());
                    return false;
                }

                if (proposal != null) {
                    ProposalStatus oldStatus = proposal.getStatus();
                    switch (oldStatus) {
                        case CONSIDERATION:
                            if (ProposalStatus.CANCELED.equals(newStatus)) {
                                proposal.setStatus(newStatus);
                                proposalDao.update(proposal);
                                log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                                return true;
                            }
                            break;
                        case APPROVED:
                            if (ProposalStatus.CANCELED.equals(newStatus)) {
                                proposal.setStatus(newStatus);
                                proposalDao.update(proposal);
                                log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                                return true;
                            } else if (ProposalStatus.ACCEPTED_BY_CONTRACTOR.equals(newStatus)) {

                                Chapter chapter = proposal.getChapter();
                                Contractor contractor = proposal.getContractor();

                                chapter.setContractor(contractor);
                                chapter.setStatus(ChapterStatus.OCCUPIED);
                                proposal.setStatus(newStatus);

                                chapterDao.executeInOneVoidTransaction(() -> chapterDao.update(chapter));
                                proposalDao.update(proposal);
                                log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                                return true;
                            }
                            break;
                        case CANCELED:
                            if (ProposalStatus.CONSIDERATION.equals(newStatus)) {
                                proposal.setStatus(newStatus);
                                proposalDao.update(proposal);
                                log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                                return true;
                            }
                            break;
                    }
                }
                log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + dto.getId() + newStatus);
                return false;
            });
            if (!isUpdated) {
                throw new NotUpdateDataInDbException();
            }
            log.trace(PROPOSAL_STATUS_CHANGED + dto.getId());
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.debug(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = PROPOSAL_STATUS_NOT_UPDATE;
            log.debug(PROPOSAL_STATUS_NOT_UPDATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, dto.getStatus(),
            exceptionMessage, null, null, null, SLASH_STRING + GET_ALL_MY_PROPOSALS_CONTRACTOR_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> createProposal(CreateRequestDto dto) {

        String exceptionMessage = null;
        try {
            Proposal proposal = proposalDao.executeInOneEntityTransaction(() -> {
                Proposal proposalFromDB;
                try {
                    proposalFromDB = proposalDao.getProposalByChapterIdContractorId(dto.getId(), dto.getSecondId());
                    log.debug(DATA_ALREDY_EXIST_IN_DB_ID + proposalFromDB.getId());
                    return proposalFromDB;
                } catch (NoResultException e) {
                    log.trace(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }

                Chapter chapter;
                try {
                    chapter = chapterDao.get(dto.getId());
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHAPTER_ID + dto.getId());
                    return null;
                } finally {
                    chapterDao.closeManager();
                }
                Contractor contractor;
                try {
                    contractor = contractorDao.get(dto.getSecondId());
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + dto.getSecondId());
                    return null;
                } finally {
                    contractorDao.closeManager();
                }
                if (chapter != null && contractor != null) {
                    boolean isAnyProposalApproved = proposalDao.isAnyProposalOfChapterApproved(dto.getId());
                    Proposal newProposal = Proposal.builder()
                                               .chapter(chapter)
                                               .contractor(contractor)
                                               .status(isAnyProposalApproved ?
                                                           ProposalStatus.REJECTED
                                                           : ProposalStatus.CONSIDERATION)
                                               .build();
                    proposalDao.create(newProposal);
                    return newProposal;
                }
                return null;
            });
            if (proposal == null) {
                throw new NotCreateDataInDbException();
            }
            log.trace(CREATED_PROPOSAL_WITH_ID + proposal.getId());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = PROPOSAL_NOT_CREATE;
            log.debug(PROPOSAL_NOT_CREATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_FREE_CHAPTERS_CONTRACTOR_SERVLET, null);
    }
}

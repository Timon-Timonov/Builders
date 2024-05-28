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
import it.academy.service.DeveloperService;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.JspURLs.*;
import static it.academy.util.constants.Messages.*;
import static it.academy.util.constants.ServletURLs.*;

@Log4j2
public class DeveloperServiceImpl implements DeveloperService {

    private final ContractorDao contractorDao = new ContractorDaoImpl();
    private final DeveloperDao developerDao = new DeveloperDaoImpl();
    private final CalculationDao calculationDao = new CalculationDaoImpl();
    private final ChapterDao chapterDao = new ChapterDaoImpl();
    private final MoneyTransferDao moneyTransferDao = new MoneyTransferDaoImpl();
    private final ProjectDao projectDao = new ProjectDaoImpl();
    private final ProposalDao proposalDao = new ProposalDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    private static DeveloperServiceImpl instance;

    private DeveloperServiceImpl() {
    }

    public static synchronized DeveloperService getInstance() {

        if (instance == null) {
            instance = new DeveloperServiceImpl();
        }
        return instance;
    }

    @Override
    public LoginDto createDeveloper(CreateRequestDto dto) {

        String exceptionMessage = null;
        UserDto userFromDb = null;

        try {
            Developer createdDeveloper = developerDao.executeInOneEntityTransaction(() -> {
                Developer developer;
                User userFromDB = null;
                try {
                    userFromDB = userDao.executeInOneEntityTransaction(() -> {
                        User newUser;
                        try {
                            newUser = User.builder()
                                          .email(dto.getEmail())
                                          .password(dto.getPassword())
                                          .role(Roles.DEVELOPER)
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
                    Developer newDeveloper = Developer.builder()
                                                 .name(dto.getName())
                                                 .address(Address.builder()
                                                              .city(dto.getCity())
                                                              .street(dto.getStreet())
                                                              .building(dto.getBuilding())
                                                              .build())
                                                 .user(userFromDB)
                                                 .build();
                    developerDao.create(newDeveloper);
                    log.trace(DEVELOPER_CREATED_ID + newDeveloper.getId());
                    developer = newDeveloper;
                } else {
                    throw new EmailOccupaidException(EMAIL + dto.getEmail() + OCCUPIED);
                }
                if (developer.getId() == null) {
                    throw new NotCreateDataInDbException();
                }
                return developer;
            });
            if (createdDeveloper == null) {
                throw new NotCreateDataInDbException();
            }
            userFromDb = UserConverter.convertToDto(createdDeveloper.getUser());
            log.trace(ACCOUNT + CREATED_SUCCESSFUL + createdDeveloper.getId());
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
                           .url(DEVELOPER_PAGES_MAIN_JSP)
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
            exceptionMessage, null, null, list, DEVELOPER_PAGES_LIST_WITH_PROJECTS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ContractorDto> getMyContractors(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ContractorDto> list = new ArrayList<>();
        Integer count = dto.getCount();
        Integer lastPageNumber = FIRST_PAGE_NUMBER;
        Integer page = FIRST_PAGE_NUMBER;
        String search = PER_CENT_STRING + dto.getSearch() + PER_CENT_STRING;
        ProjectStatus status = null;

        try {
            ProjectStatus projectStatus = (ProjectStatus) dto.getStatus();
            Page<Contractor> contractorPage = contractorDao.executeInOnePageTransaction(() -> {
                Page<Contractor> page1 = null;
                try {
                    long totalCount = contractorDao.getCountOfContractorsByDeveloperId(dto.getId(), projectStatus, search);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    int correctPage = page1.getPageNumber();
                    page1.setMap(contractorDao.getContractorsByDeveloperId(dto.getId(), projectStatus, search, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + projectStatus, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            page = contractorPage.getPageNumber();
            status = projectStatus;
            lastPageNumber = contractorPage.getLastPageNumber();
            Map<Contractor, Integer[]> map = contractorPage.getMap();
            list.addAll(map.keySet().stream()
                            .map(contractor -> {
                                Integer[] values = map.get(contractor);
                                return ContractorConverter.convertToDto(contractor, values[0] - values[1]);
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
            exceptionMessage, null, null, list, DEVELOPER_PAGES_LIST_WITH_CONTRACTORS_JSP, dto.getSearch());
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getAllMyProposals(FilterPageDto dto) {

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
                    long totalCount = proposalDao.getCountOfProposalsByDeveloperId(dto.getId(), proposalStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList()
                        .addAll(proposalDao.getProposalsByDeveloperId(dto.getId(), proposalStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
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
            exceptionMessage, null, null, list, DEVELOPER_PAGES_LIST_WITH_ALL_PROPOSALS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ProjectDto> createProject(CreateRequestDto dto) {

        String exceptionMessage = null;
        List<ProjectDto> list = new ArrayList<>();

        Project project = null;
        try {
            project = projectDao.executeInOneEntityTransaction(() -> {
                Developer developer = null;
                try {
                    developer = developerDao.get(dto.getId());
                } catch (EntityNotFoundException e) {
                    log.error(DEVELOPER_NOT_FOUND_WITH_ID + dto.getId());
                } finally {
                    developerDao.closeManager();
                }
                if (developer != null) {
                    Project newProject = Project.builder()
                                             .developer(developer)
                                             .name(dto.getName())
                                             .address(Address.builder()
                                                          .city(dto.getCity())
                                                          .street(dto.getStreet())
                                                          .building(dto.getBuilding())
                                                          .build())
                                             .build();
                    projectDao.create(newProject);
                    return (newProject);
                }
                return null;
            });
            if (project == null) {
                throw new NotCreateDataInDbException();
            }
            list.add(ProjectConverter.convertToDto(project, null, null));
            log.trace(PROJECT + CREATED_SUCCESSFUL + project.getId());
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = PROJECT_NOT_CREATE;
            log.debug(PROJECT_NOT_CREATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        project = project != null ? project : new Project();
        return new DtoWithPageForUi<>(null, null, null, project.getStatus(),
            exceptionMessage, project.getId(), project.getName(), list, DEVELOPER_PAGES_CREATE_CHAPTER_PAGE_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> createChapter(CreateRequestDto dto) {

        String exceptionMessage = null;
        try {
            Chapter chapter = chapterDao.executeInOneEntityTransaction(() -> {
                Project project = null;
                try {
                    project = projectDao.get(dto.getId());
                } catch (EntityNotFoundException e) {
                    log.error(PROJECT_NOT_FOUND_WITH_ID + dto.getId());
                } finally {
                    projectDao.closeManager();
                }
                if (project != null) {
                    Chapter newChapter = Chapter.builder()
                                             .project(project)
                                             .name(dto.getName())
                                             .price(dto.getInt1())
                                             .build();
                    chapterDao.create(newChapter);
                    return newChapter;
                }
                return null;
            });

            if (chapter == null) {
                throw new NotCreateDataInDbException();
            }
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = CHAPTER_NOT_CREATE;
            log.debug(PROJECT_NOT_CREATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> cancelChapter(Long chapterId) {

        String exceptionMessage = null;

        try {
            boolean isUpdated = chapterDao.executeInOneBoolTransaction(() -> {
                Chapter chapter = null;
                try {
                    chapter = chapterDao.get(chapterId);
                    log.trace(CHAPTER_READ_FROM_DB + chapter.getId());
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + chapterId);
                }
                if (chapter != null) {
                    chapter.setStatus(ChapterStatus.CANCELED);
                    chapterDao.update(chapter);
                    log.trace(CHAPTER_STATUS_CHANGED + chapterId + ChapterStatus.CANCELED);
                    proposalDao.executeInOneVoidTransaction(() -> {
                        proposalDao.rejectAllConsiderateProposalsOfChapter(chapterId);
                        log.trace(PROPOSAL_STATUSES_OF_ALL_CHAPTER_CHANGED_CHAPTER_ID + chapterId + ProposalStatus.REJECTED);
                    });
                    return true;
                }
                return false;
            });
            if (!isUpdated) {
                throw new NotUpdateDataInDbException();
            }
            log.trace(CHAPTER_STATUS_CHANGED + chapterId);
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = CHANGING_OF_CHAPTER_STATUS_FAILED;
            log.error(CHANGING_OF_CHAPTER_STATUS_FAILED + chapterId, e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByProject(FilterPageDto dto) {

        String exceptionMessage = null;
        List<ChapterDto> list = new ArrayList<>();

        try {
            Page<Chapter> page = ((chapterDao.executeInOnePageTransaction(() -> {
                Page<Chapter> page1 = new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
                try {
                    page1.setMap(chapterDao.getChaptersByProjectId(dto.getId()));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1;
            })));
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
            exceptionMessage, dto.getId(), dto.getName(), list, DEVELOPER_PAGES_LIST_WITH_CHAPTERS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ChapterDto> getChaptersByContractorIdAndDeveloperId(FilterPageDto dto) {

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
                    long totalCount = chapterDao.getCountOfChaptersByContractorIdAndDeveloperId(
                        dto.getSecondId(), dto.getId(), projectStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.setMap(chapterDao.getChaptersByContractorIdAndDeveloperId(
                        dto.getSecondId(), dto.getId(), projectStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_DEVELOPER_ID + dto.getSecondId());
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
            status = projectStatus;
            page = chapterPage.getPageNumber();
            lastPageNumber = chapterPage.getLastPageNumber();
            Map<Chapter, Integer[]> map = chapterPage.getMap();
            list.addAll(map.keySet().stream()
                            .map(chapter -> {
                                Integer[] values = map.get(chapter);
                                return ChapterConverter.convertToDto(chapter, values[0] - values[1]);
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
            exceptionMessage, dto.getId(), dto.getName(), list, DEVELOPER_PAGES_LIST_WITH_CHAPTERS_BY_ONE_CONTRACTOR_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> changeStatusOfProposal(ChangeRequestDto dto) {

        String exceptionMessage = null;
        String url = Boolean.TRUE.toString().equalsIgnoreCase(dto.getName()) ?
                         SLASH_STRING + GET_MY_PROPOSALS_FROM_CHAPTER_DEVELOPER_SERVLET
                         : SLASH_STRING + GET_ALL_MY_PROPOSALS_DEVELOPER_SERVLET;
        Long proposalId = dto.getId();

        try {
            ProposalStatus newStatus = (ProposalStatus) dto.getStatus();
            boolean isUpdated = proposalDao.executeInOneBoolTransaction(() -> {
                Proposal proposal;
                ProposalStatus currentStatus;
                try {
                    proposal = proposalDao.get(proposalId);
                    log.trace(PROPOSAL_READ_FROM_DB + proposal.getId());
                    currentStatus = proposal.getStatus();
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_PROPOSAL_ID + proposalId);
                    return false;
                }

                switch (currentStatus) {
                    case CONSIDERATION:
                        if (ProposalStatus.REJECTED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_CHANGED + proposal.getId() + newStatus);
                            return true;
                        } else if (ProposalStatus.APPROVED.equals(newStatus)) {

                            Chapter chapter = proposal.getChapter();
                            proposalDao.rejectAllConsiderateProposalsOfChapter(chapter.getId());
                            log.trace(PROPOSAL_STATUSES_OF_ALL_CHAPTER_CHANGED_CHAPTER_ID + chapter.getId() + ProposalStatus.REJECTED);
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_CHANGED + proposalId + ProposalStatus.APPROVED);
                            return true;
                        }
                        break;
                    case APPROVED:
                        if (ProposalStatus.REJECTED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_CHANGED + proposal.getId() + newStatus);
                            return true;
                        }
                        break;
                    case REJECTED:
                        if (ProposalStatus.CONSIDERATION.equals(newStatus)) {
                            Chapter chapter = proposal.getChapter();
                            if (ChapterStatus.FREE.equals(chapter.getStatus())
                                    && chapter.getContractor() == null
                                    && !proposalDao.isAnyProposalOfChapterApproved(chapter.getId())) {
                                proposal.setStatus(newStatus);
                                proposalDao.update(proposal);
                                log.trace(PROPOSAL_STATUS_CHANGED + proposal.getId() + newStatus);
                                return true;
                            } else {
                                log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + proposal.getId() + newStatus);
                            }
                        }
                        break;
                }
                return false;
            });
            if (!isUpdated) {
                throw new NotUpdateDataInDbException();
            }
            log.trace(PROPOSAL_STATUS_CHANGED + dto.getId() + newStatus);
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
            exceptionMessage, null, null, null, url, null);
    }

    @Override
    public DtoWithPageForUi<ProposalDto> getProposalsByChapter(FilterPageDto dto) {

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
                    long totalCount = proposalDao.getCountOfProposalsByChapterId(dto.getId(), proposalStatus);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.getList().addAll(proposalDao.getProposalsByChapterId(
                        dto.getId(), proposalStatus, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + dto.getId());
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
            exceptionMessage, dto.getId(), dto.getName(), list, DEVELOPER_PAGES_LIST_WITH_PROPOSALS_OF_CHAPTER_JSP, null);
    }

    @Override
    public DtoWithPageForUi<ProjectDto> changeProjectStatus(ChangeRequestDto dto) {

        String exceptionMessage = null;

        try {
            ProjectStatus newStatus = (ProjectStatus) dto.getStatus();
            boolean isUpdated = projectDao.executeInOneBoolTransaction(() -> {
                Project project = null;
                boolean isProjectStatusChanged = false;
                try {
                    project = projectDao.get(dto.getId());
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_PROJECT_ID + dto.getId());
                }
                if (project != null) {
                    switch (project.getStatus()) {
                        case PREPARATION:
                            if (ProjectStatus.IN_PROCESS.equals(newStatus) ||
                                    ProjectStatus.CANCELED.equals(newStatus)) {
                                project.setStatus(newStatus);
                                projectDao.update(project);
                                log.trace(PROJECT_STATUS_CHANGED + project.getId() + newStatus);
                                isProjectStatusChanged = true;
                            }
                            break;
                        case IN_PROCESS:
                            if (ProjectStatus.COMPLETED.equals(newStatus) ||
                                    ProjectStatus.CANCELED.equals(newStatus)) {
                                project.setStatus(newStatus);
                                projectDao.update(project);
                                log.trace(PROJECT_STATUS_CHANGED + project.getId() + newStatus);
                                isProjectStatusChanged = true;
                            }
                            break;
                    }
                    if (ProjectStatus.CANCELED.equals(newStatus)) {
                        if (isProjectStatusChanged) {

                            try {
                                chapterDao.executeInOneVoidTransaction(
                                    () -> chapterDao.cancelChaptersByProjectId(dto.getId()));
                            } catch (Exception e) {
                                log.error(CHANGING_OF_CHAPTER_STATUS_FAILED, e);
                                throw new RollbackException(CHANGING_OF_CHAPTER_STATUS_FAILED);
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return isProjectStatusChanged;
                    }
                }
                return false;
            });

            if (!isUpdated) {
                throw new NotUpdateDataInDbException();
            }
            log.trace(PROJECT_STATUS_CHANGED + dto.getId() + newStatus);
        } catch (ClassCastException e) {
            exceptionMessage = INVALID_VALUE;
            log.debug(INVALID_VALUE + dto.getStatus().toString(), e);
        } catch (NotUpdateDataInDbException e) {
            exceptionMessage = PROJECT_STATUS_NOT_UPDATE;
            log.debug(PROJECT_STATUS_NOT_UPDATE + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_CHAPTERS_OF_PROJECT_DEVELOPER_SERVLET, null);
    }

    @Override
    public DtoWithPageForUi<CalculationDto> getCalculationsByChapterId(FilterPageDto dto) {

        String exceptionMessage = null;
        List<CalculationDto> list = new ArrayList<>();
        Integer page = null;
        Integer lastPageNumber = null;

        try {
            Page<Calculation> calculationPage = calculationDao.executeInOnePageTransaction(() -> {
                Page<Calculation> page1 = null;
                try {
                    long chapterId = dto.getId();
                    int count = dto.getCount();
                    long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
                    page1 = Util.getPageWithCorrectNumbers(dto.getPage(), count, totalCount);
                    page1.setMap(calculationDao.getCalculationsByChapterId(
                        dto.getId(), page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + dto.getId());
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
        return new DtoWithPageForUi<>(page, dto.getCount(), lastPageNumber, null,
            exceptionMessage, dto.getId(), dto.getName(), list, DEVELOPER_PAGES_LIST_WITH_CALCULATIONS_JSP, null);
    }

    @Override
    public DtoWithPageForUi<MoneyTransferDto> payMoney(CreateRequestDto dto) {

        long calculationId = dto.getId();
        int sumAdvance = dto.getInt1();
        int sumForWork = dto.getInt2();
        int debt = dto.getInt3();
        String exceptionMessage = null;

        try {
            PaymentType type = null;
            int sum = ZERO_INT_VALUE;
            if (sumAdvance > ZERO_INT_VALUE) {
                type = PaymentType.ADVANCE_PAYMENT;
                sum = sumAdvance;
            } else if (sumForWork > ZERO_INT_VALUE) {
                type = PaymentType.PAYMENT_FOR_WORK;
                sum = sumForWork;
                if (sum > debt) {
                    throw new NotCreateDataInDbException();
                }
            }

            final int sum1 = sum;
            final PaymentType paymentType = type;

            boolean isCreated = moneyTransferDao.executeInOneBoolTransaction(() -> {
                Calculation calculation = null;
                try {
                    calculation = calculationDao.get(calculationId);
                } catch (EntityNotFoundException e) {
                    log.error(CALCULATION_NOT_FOUND_WITH_ID + calculationId, e);
                } finally {
                    calculationDao.closeManager();
                }
                if (calculation != null) {
                    MoneyTransfer newTransfer = MoneyTransfer.builder()
                                                    .calculation(calculation)
                                                    .sum(sum1)
                                                    .type(paymentType)
                                                    .build();
                    moneyTransferDao.create(newTransfer);
                    return true;
                }
                return false;
            });
            if (!isCreated) {
                throw new NotCreateDataInDbException();
            }
        } catch (NotCreateDataInDbException e) {
            exceptionMessage = NOT_SUCCESS_OPERATION;
            log.debug(NOT_SUCCESS_OPERATION + dto.toString(), e);
        } catch (IOException e) {
            exceptionMessage = BAD_CONNECTION;
            log.error(BAD_CONNECTION, e);
        } catch (Exception e) {
            exceptionMessage = SOMETHING_WENT_WRONG;
            log.error(SOMETHING_WENT_WRONG, e);
        }
        return new DtoWithPageForUi<>(null, null, null, null,
            exceptionMessage, null, null, null, SLASH_STRING + GET_MY_CALCULATION_DEVELOPER_SERVLET, null);
    }
}

package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.service.dto.Page;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
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

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;
import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Messages.*;

@Log4j2
public class ContractorServiceImpl implements ContractorService {


    private final ContractorDao contractorDao = new ContractorDaoImpl();
    private final DeveloperDao developerDao = new DeveloperDaoImpl();
    private final CalculationDao calculationDao = new CalculationDaoImpl();
    private final ChapterDao chapterDao = new ChapterDaoImpl();
    private final ProjectDao projectDao = new ProjectDaoImpl();
    private final ProposalDao proposalDao = new ProposalDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Contractor createContractor(
        String email, String password, String name, String city, String street, String building)
        throws Exception {

        Contractor createdContractor;
        try {
            createdContractor = contractorDao.executeInOneEntityTransaction(() -> {
                Contractor contractor;
                User userFromDB = null;
                try {
                    userFromDB = userDao.executeInOneEntityTransaction(() -> {
                        User newUser;
                        try {
                            newUser = User.builder()
                                          .email(email)
                                          .password(password)
                                          .role(Roles.CONTRACTOR)
                                          .status(UserStatus.ACTIVE)
                                          .build();
                            userDao.create(newUser);
                        } catch (ConstraintViolationException e) {
                            log.error(EMAIL + email + IS_OCCUPIED, e);
                            return null;
                        }
                        return newUser;
                    });
                } catch (RollbackException e) {
                    log.error(e);
                }

                if (userFromDB != null) {
                    Contractor newContractor = Contractor.builder()
                                                   .name(name)
                                                   .address(Address.builder()
                                                                .city(city)
                                                                .street(street)
                                                                .building(building)
                                                                .build())
                                                   .user(userFromDB)
                                                   .build();
                    contractorDao.create(newContractor);
                    log.trace(CONTRACTOR_CREATED_ID + newContractor.getId());
                    contractor = newContractor;
                } else {
                    throw new EmailOccupaidException(EMAIL + email + OCCUPIED);
                }
                if (contractor.getId() == null) {
                    throw new NotCreateDataInDbException();
                }
                return contractor;
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return createdContractor;
    }

    @Override
    public Contractor getContractor(long userId) throws Exception {

        Contractor contractorFromDb;
        try {
            contractorFromDb = contractorDao.executeInOneEntityTransaction(() -> {
                Contractor contractor = null;
                User user;
                try {
                    user = userDao.get(userId);
                } catch (EntityNotFoundException e) {
                    log.error(CONTRACTOR_NOT_FOUND_WITH_ID + userId);
                    throw e;
                }
                if (user != null) {
                    if (Roles.CONTRACTOR.equals(user.getRole())) {
                        contractor = ((Contractor) user.getLegalEntity());
                    } else {
                        log.error(USER_NOT_CONTRACTOR_ID + userId);
                        throw new RoleException(USER_NOT_CONTRACTOR);
                    }
                }
                return contractor;
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return contractorFromDb;
    }

    @Override
    public Page<Project> getMyProjects(long contractorId, ProjectStatus status, int page, int count)
        throws Exception {

        Page<Project> projectPage;
        try {
            projectPage = projectDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Project> list = new ArrayList<>();
                try {
                    long totalCount = projectDao.getCountOfProjectsByContractorId(contractorId, status);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(projectDao.getProjectsByContractorId(contractorId, status, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return projectPage;
    }

    @Override
    public Page<Project> getMyProjectsByDeveloper(
        long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws Exception {

        Page<Project> projectPage;
        try {
            projectPage = projectDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Project> list = new ArrayList<>();
                try {
                    long totalCount = projectDao.getCountOfProjectsByDeveloperIdContractorId(developerId, contractorId, status);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(projectDao.getProjectsByDeveloperIdContractorId(developerId, contractorId, status, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId +
                                  AND_DELELOPER_ID + developerId);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return projectPage;
    }

    @Override
    public List<String> getAllChapterNames() throws IOException {

        List<String> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getAllChapterNames());
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB);
        } finally {
            chapterDao.closeManager();
        }
        return list;
    }

    @Override
    public Page<Chapter> getFreeChapters(long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws Exception {

        Page<Chapter> chapterPage;
        try {
            chapterPage = chapterDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Chapter> list = new ArrayList<>();
                try {
                    long totalCount = chapterDao.getCountOfFreeChaptersByName(contractorId, chapterName, projectStatus);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(chapterDao.getFreeChapters(contractorId, chapterName, projectStatus, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return chapterPage;
    }

    @Override
    public Page<Developer> getMyDevelopers(long contractorId, ProjectStatus status, int page, int count)
        throws Exception {

        Page<Developer> developerPage;
        try {
            developerPage = developerDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Developer> list = new ArrayList<>();
                try {
                    long totalCount = developerDao.getCountOfDevelopers(contractorId, status);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(developerDao.getDevelopersByContractorId(contractorId, status, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return developerPage;
    }

    @Override
    public Page<Proposal> getMyProposals(long contractorId, ProposalStatus status, int page, int count)
        throws Exception {

        Page<Proposal> proposalPage;
        try {
            proposalPage = proposalDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Proposal> list = new ArrayList<>();
                try {
                    long totalCount = proposalDao.getCountOfProposalsByContractorId(contractorId, status);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(proposalDao.getProposalsByContractorId(contractorId, status, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return proposalPage;
    }

    @Override
    public List<Chapter> getMyChaptersByProjectId(long projectId, long contractorId)
        throws Exception {

        List<Chapter> chapterList;
        try {
            chapterList = chapterDao.executeInOneListTransaction(() -> {
                List<Chapter> list = new ArrayList<>();
                try {
                    list.addAll(chapterDao.getChaptersByProjectIdContractorId(projectId, contractorId));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return list;
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return chapterList;
    }

    @Override
    public Page<Calculation> getCalculationsByChapter(long chapterId, int page, int count)
        throws Exception {

        Page<Calculation> calculationPage;
        try {
            calculationPage = calculationDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Calculation> list = new ArrayList<>();
                try {
                    long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(calculationDao.getCalculationsByChapterId(chapterId, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return calculationPage;
    }

    @Override
    public void updateWorkPriceFact(int workPriceFact, long calculationId)
        throws Exception {

        boolean isUpdated;
        try {
            isUpdated = calculationDao.executeInOneBoolTransaction(() -> {
                int count = calculationDao.updateWorkPriceFact(workPriceFact, calculationId);
                if (1 == count) {
                    log.trace(WORKPRICE_UPDATED_ID + calculationId + VALUE + workPriceFact);
                    return true;
                } else {
                    log.debug(WORKPRICE_NOT_UPDATED_ID + calculationId);
                    return false;
                }
            });
        } catch (RollbackException e) {
            log.error(CALCULATION_NOT_UPDATED, e);
            throw new NotCreateDataInDbException();
        }
        if (!isUpdated) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Calculation createCalculation(long chapterId, int YYYY, int MM, int workPricePlan)
        throws Exception {

        Calculation calculation;
        try {
            calculation = calculationDao.executeInOneEntityTransaction(() -> {
                Chapter chapter;
                try {
                    chapter = chapterDao.get(chapterId);
                } catch (EntityNotFoundException e) {
                    log.error(CHAPTER_NOT_FOUND_ID + chapterId, e);
                    return null;
                } finally {
                    chapterDao.closeManager();
                }
                if (chapter != null && ChapterStatus.OCCUPIED.equals(chapter.getStatus())) {
                    Calculation newCalculation = Calculation.builder()
                                                     .chapter(chapter)
                                                     .month(Date.valueOf("" + YYYY + "-" + MM + "-" + "01"))
                                                     .workPricePlan(workPricePlan)
                                                     .build();
                    calculationDao.create(newCalculation);
                    log.trace(CALCULATION_CREATED_ID + newCalculation.getId());
                    return newCalculation;
                }
                log.error(CALCULATION_NOT_CREATED);
                return null;
            });
        } catch (RollbackException e) {
            log.error(CALCULATION_NOT_CREATED, e);
            throw new NotCreateDataInDbException();
        }
        if (calculation == null) {
            throw new NotCreateDataInDbException();
        }
        return calculation;
    }

    @Override
    public void setProposalStatus(long proposalId, ProposalStatus newStatus) throws Exception {

        if (newStatus == null) {
            log.error(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId + NEW_STATUS + newStatus);
            throw new NotUpdateDataInDbException();
        }

        boolean isUpdated;
        try {
            isUpdated = proposalDao.executeInOneBoolTransaction(() -> {
                Proposal proposal;
                try {
                    proposal = proposalDao.get(proposalId);
                } catch (EntityNotFoundException e) {
                    log.error(PROPOSAL_NOT_FOUND_ID + proposalId);
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
                log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId);
                return false;
            });
        } catch (RollbackException e) {
            log.error(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId + NEW_STATUS + newStatus, e);
            throw new NotCreateDataInDbException();
        }
        if (!isUpdated) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Proposal createProposal(long chapterId, long contractorId)
        throws Exception {

        Proposal proposal;
        try {
            proposal = proposalDao.executeInOneEntityTransaction(() -> {
                Proposal proposalFromDB;
                try {
                    proposalFromDB = proposalDao.getProposalByChapterIdContractorId(chapterId, contractorId);
                    log.debug(DATA_ALREDY_EXIST_IN_DB_ID + proposalFromDB.getId());
                    return proposalFromDB;
                } catch (NoResultException e) {
                    log.trace(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }

                Chapter chapter;
                try {
                    chapter = chapterDao.get(chapterId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHAPTER_ID + chapterId);
                    return null;
                } finally {
                    chapterDao.closeManager();
                }

                Contractor contractor;
                try {
                    contractor = contractorDao.get(contractorId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
                    return null;
                } finally {
                    contractorDao.closeManager();
                }

                if (chapter != null && contractor != null) {
                    boolean isAnyProposalApproved = proposalDao.isAnyProposalOfChapterApproved(chapterId);
                    Proposal newProposal = Proposal.builder()
                                               .chapter(chapter)
                                               .contractor(contractor)
                                               .status(isAnyProposalApproved ?
                                                           ProposalStatus.REJECTED
                                                           : ProposalStatus.CONSIDERATION)
                                               .build();
                    proposalDao.create(newProposal);
                    log.trace(CREATED_PROPOSAL_WITH_ID + newProposal.getId());
                    return newProposal;
                }
                return null;
            });
        } catch (RollbackException e) {
            log.error(PROPOSAL_NOT_CREATE, e);
            throw new NotCreateDataInDbException();
        }

        if (proposal == null) {
            throw new NotCreateDataInDbException();
        }
        return proposal;
    }

    @Override
    public int getTotalDeptByDeveloper(long contractorId, long developerId) throws IOException {

        int debt = ZERO_INT_VALUE;
        try {
            debt = chapterDao.getAllChaptersByDeveloperIdContractorId(developerId, contractorId)
                       .stream()
                       .map(Util::getDebtByChapter)
                       .reduce(0, Integer::sum);
        } catch (NoResultException | IOException e) {
            log.debug(e);
        } finally {
            chapterDao.closeManager();
        }
        return debt;
    }
}

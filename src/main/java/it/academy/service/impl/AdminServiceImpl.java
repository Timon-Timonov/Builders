package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.*;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.AdminService;
import it.academy.service.dto.Page;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;
import static it.academy.util.constants.Messages.*;

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

    @Override
    public User getUser(String email) throws Exception {

        User userFromDb;
        try {
            userFromDb = userDao.executeInOneEntityTransaction(() -> {
                User user = null;
                try {
                    user = userDao.getUser(email);
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL + email, e);
                }
                return user;
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return userFromDb;
    }

    @Override
    public User createAdmin(String email, String password) throws Exception {

        User userCreated;
        try {
            userCreated = userDao.executeInOneEntityTransaction(() -> {
                User user = null;
                try {
                    user = userDao.getUser(email);
                } catch (NoResultException e) {
                    log.trace(EMAIL + email + NOT_OCCUPIED, e);
                }
                if (user != null) {
                    log.trace(EMAIL + email + OCCUPIED);
                    throw new EmailOccupaidException(EMAIL + IS_OCCUPIED);
                } else {
                    User newUser = User.builder()
                                       .email(email)
                                       .password(password)
                                       .role(Roles.ADMIN)
                                       .status(UserStatus.ACTIVE)
                                       .build();
                    userDao.create(newUser);
                    return newUser;
                }
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return userCreated;
    }

    @Override
    public void changeUserStatus(long userId, UserStatus newStatus) throws Exception {

        boolean isUpdated;
        try {
            isUpdated = userDao.executeInOneBoolTransaction(() -> {
                User user = null;
                try {
                    user = userDao.get(userId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_ID + userId);
                }
                if (user != null) {
                    user.setStatus(newStatus);
                    userDao.update(user);
                    log.trace(USER_STATUS_CHANED_USER_ID + userId + newStatus.toString());
                    return true;
                }
                return false;
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        if (!isUpdated) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Page<Project> getProjectsByDeveloper(long developerId, ProjectStatus status, int page, int count) throws Exception {

        Page<Project> projectPage;

        try {
            projectPage = projectDao.executeInOnePageTransaction(() -> {
                Page<Project> page1 = null;
                try {
                    long totalCount = projectDao.getCountOfProjectsByDeveloperId(developerId, status);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(projectDao.getProjectsByDeveloperId(
                        developerId, status, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return projectPage;
    }

    @Override
    public List<Chapter> getChaptersByProjectId(long projectId) throws Exception {

        List<Chapter> chapterList = new ArrayList<>();
        try {
            chapterList.addAll(chapterDao.executeInOneListTransaction(
                () -> new ArrayList<>(chapterDao.getChaptersByProjectId(projectId))));
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
        }
        return chapterList;
    }

    @Override
    public Page<Chapter> getChaptersByContractorId(long contractorId, int page, int count) throws Exception {

        Page<Chapter> chapterPage;

        try {
            chapterPage = chapterDao.executeInOnePageTransaction(() -> {
                Page<Chapter> page1 = null;
                try {
                    long totalCount = chapterDao.getCountOfChaptersByContractorId(contractorId);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(chapterDao.getChaptersByContractorId(
                        contractorId, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return chapterPage;
    }

    @Override
    public Page<Calculation> getCalculationsByChapterId(long chapterId, int page, int count)
        throws Exception {

        Page<Calculation> calculationPage;
        try {
            calculationPage = calculationDao.executeInOnePageTransaction(() -> {
                Page<Calculation> page1 = null;
                try {
                    long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(calculationDao.getCalculationsByChapterId(
                        chapterId, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return calculationPage;
    }

    @Override
    public List<MoneyTransfer> getMoneyTransfers(long calculationId) throws Exception {

        List<MoneyTransfer> list = new ArrayList<>();
        try {
            list.addAll(moneyTransferDao.executeInOneListTransaction(
                () -> moneyTransferDao.getMoneyTransfersByCalculationId(calculationId)));
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
        }
        return list;
    }

    @Override
    public Page<Proposal> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage;
        try {
            proposalPage = proposalDao.executeInOnePageTransaction(() -> {
                Page<Proposal> page1 = null;
                try {
                    long totalCount = proposalDao.getCountOfProposalsByChapterId(chapterId, status);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(proposalDao.getProposalsByChapterId(
                        chapterId, status, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + status, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return proposalPage;
    }

    @Override
    public Page<Proposal> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception {

        Page<Proposal> proposalPage;
        try {
            proposalPage = proposalDao.executeInOnePageTransaction(() -> {
                Page<Proposal> page1 = null;
                try {
                    long totalCount = proposalDao.getCountOfProposalsByContractorId(contractorId, status);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(proposalDao.getProposalsByContractorId(
                        contractorId, status, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + status, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return proposalPage;
    }

    @Override
    public List<User> getAllAdministrators() throws Exception {

        List<User> list = new ArrayList<>();
        try {
            list.addAll(userDao.executeInOneListTransaction(userDao::getAdministrators));
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
        }
        return list;
    }

    @Override
    public Page<Contractor> getAllContractors(UserStatus status, int page, int count)
        throws Exception {

        Page<Contractor> contractorPage;
        try {
            contractorPage = contractorDao.executeInOnePageTransaction(() -> {
                Page<Contractor> page1 = null;
                try {
                    long totalCount = contractorDao.getCountOfContractors(status);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(contractorDao.getContractors(status, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + status, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return contractorPage;
    }

    @Override
    public Page<Developer> getAllDevelopers(UserStatus status, int page, int count)
        throws Exception {

        Page<Developer> developerPage;
        try {
            developerPage = developerDao.executeInOnePageTransaction(() -> {
                Page<Developer> page1 = null;
                try {
                    long totalCount = developerDao.getCountOfDevelopers(status);
                    page1 = Util.getPageWithCorrectNumbers(page, count, totalCount);
                    page1.getList().addAll(developerDao.getDevelopers(status, page1.getPageNumber(), count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + status, e);
                }
                return page1 != null ? page1
                           : new Page<>(new ArrayList<>(), FIRST_PAGE_NUMBER, FIRST_PAGE_NUMBER);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return developerPage;
    }


    @Override
    public void deleteUser(long userId) throws IOException, NotUpdateDataInDbException {

        try {
            userDao.executeInOneVoidTransaction(() -> {
                try {
                    User user = userDao.get(userId);
                    Roles role = user.getRole();
                    switch (role) {
                        case CONTRACTOR:
                            contractorDao.executeInOneVoidTransaction(() -> {
                                contractorDao.delete(userId);
                                userDao.delete(userId);
                            });
                            break;
                        case DEVELOPER:
                            developerDao.executeInOneVoidTransaction(() -> {
                                developerDao.delete(userId);
                                userDao.delete(userId);
                            });
                            break;
                        case ADMIN:
                            userDao.delete(userId);
                            break;
                    }
                    log.debug(USER_DELETE_ID + userId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + userId, e);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_USER_ID + userId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteCalculation(long calculationId) throws IOException, NotUpdateDataInDbException {

        try {
            calculationDao.executeInOneVoidTransaction(() -> {
                try {
                    calculationDao.delete(calculationId);
                    log.debug(CALCULATION_DELETE_ID + calculationId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID1 + calculationId, e);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_CALCULATION_ID + calculationId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteChapter(long chapterId) throws IOException, NotUpdateDataInDbException {

        try {
            chapterDao.executeInOneVoidTransaction(() -> {
                try {
                    chapterDao.delete(chapterId);
                    log.debug(CHAPTER_DELETE_ID + chapterId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + chapterId, e);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_CHAPTER_ID + chapterId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteMoneyTransfer(long transferId) throws IOException, NotUpdateDataInDbException {

        try {
            moneyTransferDao.executeInOneVoidTransaction(() -> {
                try {
                    moneyTransferDao.delete(transferId);
                    log.debug(MONEY_TRANSFER_DELETE_ID + transferId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + transferId, e);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_MONEY_TRANSFER_ID + transferId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteProject(long projectId) throws IOException, NotUpdateDataInDbException {

        try {
            projectDao.executeInOneVoidTransaction(() -> {
                try {
                    projectDao.delete(projectId);
                    log.debug(PROJECT_DELETE_ID + projectId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + projectId, e);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_PROJECT_ID + projectId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteProposal(long proposalId) throws IOException, NotUpdateDataInDbException {

        try {
            proposalDao.executeInOneVoidTransaction(() -> {
                try {
                    proposalDao.delete(proposalId);
                    log.debug(PROPOSAL_DELETE_ID + proposalId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + proposalId, e);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_PROPOSAL_ID + proposalId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public List<Project> getAllProjects() throws Exception {

        List<Project> list = new ArrayList<>();
        try {
            list.addAll(projectDao.executeInOneListTransaction(projectDao::getAll));
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB, e);
        }
        return list;
    }
}
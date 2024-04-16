package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.dto.Page;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.Project;
import it.academy.pojo.User;
import it.academy.pojo.enums.ProjectStatus;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static it.academy.util.Constants.*;

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
    public User getUser(String email) throws IOException {

        User user = new User();
        try {
            user = userDao.getUser(email);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL + email);
        } finally {
            userDao.closeManager();
        }
        return user;
    }

    @Override
    public User createAdmin(String email, String password) throws IOException, EmailOccupaidException, NotCreateDataInDbException {

        AtomicReference<User> userFromDB = new AtomicReference<>();
        AtomicBoolean isEmailOccupaid = new AtomicBoolean(false);
        userDao.executeInOneTransaction(() -> {
            User user = null;
            try {
                user = userDao.getUser(email);
            } catch (NoResultException e) {
                log.trace(EMAIL + email + NOT_OCCUPIED, e);
            }
            if (user != null) {
                log.trace(EMAIL + email + OCCUPIED);
                isEmailOccupaid.set(true);
            } else {
                User newUser = User.builder()
                                   .email(email)
                                   .password(password)
                                   .role(Roles.ADMIN)
                                   .status(UserStatus.AKTIVE)
                                   .build();
                userDao.create(newUser);
                userFromDB.set(newUser);
            }
        });
        if (isEmailOccupaid.get()) {
            throw new EmailOccupaidException(EMAIL + email + OCCUPIED);
        } else if (userFromDB.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return userFromDB.get();
    }

    @Override
    public void cancelUser(Long userId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        userDao.executeInOneTransaction(() -> {
            User user = null;
            try {
                user = userDao.get(userId);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_ID + userId);
            }
            if (user != null) {
                user.setStatus(UserStatus.CANCELED);
                log.trace(USER_CANCELED_USER_ID + userId);
                isUpdated.set(true);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Page<Project> getAllProjects(ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Project> list = new ArrayList<>();
        try {
            long totalCount = projectDao.getCountOfProjects(status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(new ArrayList<>(projectDao.getProjects(status, correctPage, count)));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_PROJECT_STATUS + status);
        } finally {
            projectDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Page<Contractor> getAllContractors(UserStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Contractor> list = new ArrayList<>();
        try {
            long totalCount = contractorDao.getCountOfContractors(status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(contractorDao.getContractors(status, correctPage, count));
        } catch (Exception e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + status);
        } finally {
            contractorDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Page<Developer> getAllDevelopers(UserStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Developer> list = new ArrayList<>();
        try {
            long totalCount = developerDao.getCountOfDevelopers(status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list = new ArrayList<>(developerDao.getDevelopers(status, correctPage, count));
        } catch (Exception e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_USER_STATUS + status);
        } finally {
            developerDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }


    @Override
    public void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        userDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(userId);
                log.debug(USER_DELETE_ID + userId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + userId);
            } catch (ConstraintViolationException e) {
                log.error(DELETE_FAIL_USER_ID + userId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        calculationDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(calculationId);
                log.debug(CALCULATION_DELETE_ID + calculationId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID1 + calculationId);
            } catch (ConstraintViolationException e) {
                log.error(DELETE_FAIL_CALCULATION_ID + calculationId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        chapterDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(chapterId);
                log.debug(CHAPTER_DELETE_ID + chapterId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + chapterId);
            } catch (ConstraintViolationException e) {
                log.error(DELETE_FAIL_CHAPTER_ID + chapterId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        moneyTransferDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(transferId);
                log.debug(MONEY_TRANSFER_DELETE_ID + transferId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + transferId);
            } catch (ConstraintViolationException e) {
                log.error(DELETE_FAIL_MONEY_TRANSFER_ID + transferId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        projectDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(projectId);
                log.debug(PROJECT_DELETE_ID + projectId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + projectId);
            } catch (ConstraintViolationException e) {
                log.error(DELETE_FAIL_PROJECT_ID + projectId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        proposalDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(proposalId);
                log.debug(PROPOSAL_DELETE_ID + proposalId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + proposalId);
            } catch (ConstraintViolationException e) {
                log.error(DELETE_FAIL_PROPOSAL_ID + proposalId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }
}
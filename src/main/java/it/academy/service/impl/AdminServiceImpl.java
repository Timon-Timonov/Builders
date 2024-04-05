package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.pojo.Project;
import it.academy.pojo.User;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.AdminService;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
            log.error("There is no such data in DB with email=" + email);
        }
        return user;
    }

    @Override
    public void cancelUser(Long userId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        userDao.executeInOneTransaction(() -> {
            User user = null;
            try {
                user = userDao.get(userId);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with userId=" + userId);
            }
            if (user != null) {
                user.setStatus(UserStatus.CANCELED);
                log.trace("User canceled userId=" + userId);
                isUpdated.set(true);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public List<Project> getAllProjects(ProjectStatus status, int page, int count)
        throws IOException {

        List<Project> list = new ArrayList<>();
        try {
            list.addAll(new ArrayList<>(projectDao.getProjects(status, page, count)));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with projectStatus=" + status);
        }
        return list;
    }

    @Override
    public List<Contractor> getAllContractors(UserStatus status, int page, int count)
        throws IOException {

        List<Contractor> list = new ArrayList<>();
        try {
            list.addAll(contractorDao.getContractors(status, page, count));
        } catch (Exception e) {
            log.error("There is no such data in DB with userStatus=" + status);
        }
        return list;
    }

    @Override
    public List<Developer> getAllDevelopers(UserStatus status, int page, int count)
        throws IOException {

        List<Developer> list = new ArrayList<>();
        try {
            list = new ArrayList<>(developerDao.getDevelopers(status, page, count));
        } catch (Exception e) {
            log.error("There is no such data in DB with userStatus=" + status);
        }
        return list;
    }


    @Override
    public void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        userDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(userId);
                log.debug("User delete id=" + userId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with id=" + userId);
            } catch (ConstraintViolationException e) {
                log.error("delete fail user id=" + userId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException("Failed by constraint.");
        }
    }

    @Override
    public void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        calculationDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(calculationId);
                log.debug("Calculation delete id=" + calculationId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with id=" + calculationId);
            } catch (ConstraintViolationException e) {
                log.error("delete fail calculation id=" + calculationId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException("Failed by constraint.");
        }
    }

    @Override
    public void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        chapterDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(chapterId);
                log.debug("Chapter delete id=" + chapterId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with id=" + chapterId);
            } catch (ConstraintViolationException e) {
                log.error("delete failchapter id=" + chapterId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException("Failed by constraint.");
        }
    }

    @Override
    public void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        moneyTransferDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(transferId);
                log.debug("MoneyTransfer delete id=" + transferId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with id=" + transferId);
            } catch (ConstraintViolationException e) {
                log.error("delete fail moneyTransfer id=" + transferId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException("Failed by constraint.");
        }
    }

    @Override
    public void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        projectDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(projectId);
                log.debug("Project delete id=" + projectId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with id=" + projectId);
            } catch (ConstraintViolationException e) {
                log.error("delete fail project id=" + projectId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException("Failed by constraint.");
        }
    }

    @Override
    public void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isDeleted = new AtomicBoolean(false);
        proposalDao.executeInOneTransaction(() -> {
            try {
                userDao.delete(proposalId);
                log.debug("Proposal delete id=" + proposalId);
                isDeleted.set(true);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB with id=" + proposalId);
            } catch (ConstraintViolationException e) {
                log.error("delete fail proposal id=" + proposalId, e);
            }
        });
        if (!isDeleted.get()) {
            throw new NotUpdateDataInDbException("Failed by constraint.");
        }
    }
}
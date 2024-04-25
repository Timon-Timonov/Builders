package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.dto.Page;
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
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

     /*   User user =null;
        try {
            user = userDao.getUser(email);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL + email);
        } finally {
            userDao.closeManager();
        }
        return user;*/

        return (User) userDao.executeInOneTransaction(() -> {
            User user = null;
            try {
                user = userDao.getUser(email);
            } catch (NoResultException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_EMAIL + email);
            }
            return user;
        });
    }

    @Override
    public User createAdmin(String email, String password) throws Exception {


        return (User) userDao.executeInOneTransaction(() -> {
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
    }

    @Override
    public void changeUserStatus(Long userId, UserStatus newStatus) throws Exception {

        boolean isUpdated = (boolean) userDao.executeInOneTransaction(() -> {
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
        if (!isUpdated) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Page<Project> getProjectsByDeveloper(Long developerId, ProjectStatus status, int page, int count) throws Exception {

      /*  int correctPage = FIRST_PAGE_NUMBER;
        List<Project> list = new ArrayList<>();
        try {
            long totalCount = projectDao.getCountOfProjectsByDeveloperId(developerId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(new ArrayList<>(projectDao.getProjectsByDeveloperId(developerId, status, correctPage, count)));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_PROJECT_STATUS + status);
        } finally {
            projectDao.closeManager();
        }
        return new Page<>(list, correctPage);*/

        return (Page<Project>) contractorDao.executeInOneTransaction(() -> {
            long totalCount = projectDao.getCountOfProjectsByDeveloperId(developerId, status);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Project> list = new ArrayList<>(projectDao.getProjectsByDeveloperId(developerId, status, correctPage, count));
            return new Page<>(list, correctPage);
        });
    }

    @Override
    public List<Chapter> getChaptersByProjectId(Long projectId)
        throws Exception {

     /*   List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getChaptersByProjectId(projectId));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_PROJECT_ID + projectId);
        } finally {
            chapterDao.closeManager();
        }
        return list;*/


        return (List<Chapter>) chapterDao.executeInOneTransaction(
            () -> new ArrayList<>(chapterDao.getChaptersByProjectId(projectId)));
    }

    @Override
    public Page<Chapter> getChaptersByContractorId(Long contractorId, int page, int count) throws Exception {

      /*  int correctPage = FIRST_PAGE_NUMBER;
        List<Chapter> list = new ArrayList<>();
        try {
            long totalCount = chapterDao.getCountOfChaptersByContractorId(contractorId);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(chapterDao.getChaptersByContractorId(contractorId, page, count));
        } catch (Exception e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB + contractorId, e);
        } finally {
            contractorDao.closeManager();
        }
        return new Page<>(list, correctPage);*/

        return (Page<Chapter>) contractorDao.executeInOneTransaction(() -> {
            long totalCount = chapterDao.getCountOfChaptersByContractorId(contractorId);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Chapter> list = new ArrayList<>(chapterDao.getChaptersByContractorId(contractorId, page, count));
            return new Page<>(list, correctPage);
        });
    }

    @Override
    public Page<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count)
        throws Exception {

   /*     int correctPage = FIRST_PAGE_NUMBER;
        List<Calculation> list = new ArrayList<>();
        try {
            long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(calculationDao.getCalculationsByChapterId(chapterId, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + chapterId);
        } finally {
            calculationDao.closeManager();
        }
        return new Page<>(list, correctPage);*/

        return (Page<Calculation>) contractorDao.executeInOneTransaction(() -> {
            long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Calculation> list = new ArrayList<>(calculationDao.getCalculationsByChapterId(chapterId, correctPage, count));
            return new Page<>(list, correctPage);
        });
    }

    @Override
    public List<MoneyTransfer> getMoneyTransfers(Long calculationId) throws Exception {

        return (List<MoneyTransfer>) moneyTransferDao.executeInOneTransaction(
            () -> moneyTransferDao.getMoneyTransfersByCalculationId(calculationId));
    }

    @Override
    public Page<Proposal> getProposalsByChapterId(long chapterId, ProposalStatus status, int page, int count) throws Exception {

      /*  int correctPage = FIRST_PAGE_NUMBER;
        List<Proposal> list = new ArrayList<>();
        try {
            long totalCount = proposalDao.getCountOfProposalsByChapterId(chapterId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(proposalDao.getProposalsByChapterId(chapterId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + chapterId);
        } finally {
            proposalDao.closeManager();
        }
        return new Page<>(list, correctPage);*/

        return (Page<Proposal>) contractorDao.executeInOneTransaction(() -> {
            long totalCount = proposalDao.getCountOfProposalsByChapterId(chapterId, status);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Proposal> list = new ArrayList<>(proposalDao.getProposalsByChapterId(chapterId, status, correctPage, count));
            return new Page<>(list, correctPage);
        });
    }

    @Override
    public Page<Proposal> getProposalsByContractorId(long contractorId, ProposalStatus status, int page, int count) throws Exception {

       /* int correctPage = FIRST_PAGE_NUMBER;
        List<Proposal> list = new ArrayList<>();
        try {
            long totalCount = proposalDao.getCountOfProposalsByContractorId(contractorId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(proposalDao.getProposalsByContractorId(contractorId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + contractorId);
        } finally {
            proposalDao.closeManager();
        }
        return new Page<>(list, correctPage);*/

        return (Page<Proposal>) contractorDao.executeInOneTransaction(() -> {
            long totalCount = proposalDao.getCountOfProposalsByContractorId(contractorId, status);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Proposal> list = new ArrayList<>(proposalDao.getProposalsByContractorId(contractorId, status, correctPage, count));
            return new Page<>(list, correctPage);
        });
    }

    @Override
    public List<User> getAllAdministrators() throws Exception {

        return (List<User>) userDao.executeInOneTransaction(userDao::getAdministrators);
    }

    @Override
    public Page<Contractor> getAllContractors(UserStatus status, int page, int count)
        throws Exception {

        return (Page<Contractor>) contractorDao.executeInOneTransaction(() -> {
            long totalCount = contractorDao.getCountOfContractors(status);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Contractor> list = new ArrayList<>(contractorDao.getContractors(status, correctPage, count));
            return new Page<>(list, correctPage);
        });
    }

    @Override
    public Page<Developer> getAllDevelopers(UserStatus status, int page, int count)
        throws Exception {

    /*    int correctPage = FIRST_PAGE_NUMBER;
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
        return new Page<>(list, correctPage);*/

        return (Page<Developer>) developerDao.executeInOneTransaction(() -> {
            long totalCount = developerDao.getCountOfDevelopers(status);
            int correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            List<Developer> list = new ArrayList<>(developerDao.getDevelopers(status, correctPage, count));
            return new Page<>(list, correctPage);
        });
    }


    @Override
    public void deleteUser(Long userId) throws IOException, NotUpdateDataInDbException {

        try {
            userDao.executeInOneTransaction(() -> {
                try {
                    User user = userDao.get(userId);
                    Roles role = user.getRole();
                    switch (role) {
                        case CONTRACTOR:
                            contractorDao.executeInOneTransaction(() -> {
                                contractorDao.delete(userId);
                                userDao.delete(userId);
                            });
                            break;
                        case DEVELOPER:
                            developerDao.executeInOneTransaction(() -> {
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
    public void deleteCalculation(Long calculationId) throws IOException, NotUpdateDataInDbException {

        try {
            calculationDao.executeInOneTransaction(() -> {
                try {
                    calculationDao.delete(calculationId);
                    log.debug(CALCULATION_DELETE_ID + calculationId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID1 + calculationId);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_CALCULATION_ID + calculationId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteChapter(Long chapterId) throws IOException, NotUpdateDataInDbException {

        try {
            chapterDao.executeInOneTransaction(() -> {
                try {
                    chapterDao.delete(chapterId);
                    log.debug(CHAPTER_DELETE_ID + chapterId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + chapterId);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_CHAPTER_ID + chapterId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteMoneyTransfer(Long transferId) throws IOException, NotUpdateDataInDbException {

        try {
            moneyTransferDao.executeInOneTransaction(() -> {
                try {
                    moneyTransferDao.delete(transferId);
                    log.debug(MONEY_TRANSFER_DELETE_ID + transferId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + transferId);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_MONEY_TRANSFER_ID + transferId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        try {
            projectDao.executeInOneTransaction(() -> {
                try {
                    projectDao.delete(projectId);
                    log.debug(PROJECT_DELETE_ID + projectId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + projectId);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_PROJECT_ID + projectId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }

    @Override
    public void deleteProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        try {
            proposalDao.executeInOneTransaction(() -> {
                try {
                    proposalDao.delete(proposalId);
                    log.debug(PROPOSAL_DELETE_ID + proposalId);

                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_ID + proposalId);
                }
            });
        } catch (RollbackException e) {
            log.error(DELETE_FAIL_PROPOSAL_ID + proposalId, e);
            throw new NotUpdateDataInDbException(FAILED_BY_CONSTRAINT);
        }
    }


    @Override
    public List<Project> getAllProjects() throws Exception {

        return (List<Project>) projectDao.executeInOneTransaction(projectDao::getAll);
    }
}
package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
import it.academy.dto.Page;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.exceptions.NotUpdateDataInDbException;
import it.academy.exceptions.RoleException;
import it.academy.pojo.*;
import it.academy.pojo.enums.*;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;
import it.academy.service.DeveloperService;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;
import static it.academy.util.constants.Constants.ZERO_INT_VALUE;
import static it.academy.util.constants.Messages.*;

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

    @Override
    public Developer createDeveloper(
        String email, String password, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException, EmailOccupaidException {

        AtomicReference<Developer> developer = new AtomicReference<>();
        AtomicBoolean isEmailOccupied = new AtomicBoolean(false);
        developerDao.executeInOneVoidTransaction(() -> {
            AtomicReference<User> userFromDB = new AtomicReference<>();
            userDao.executeInOneVoidTransaction(() -> {
                User user = null;
                try {
                    user = userDao.getUser(email);
                } catch (NoResultException e) {
                    log.trace(EMAIL + email + NOT_OCCUPIED, e);
                }
                if (user != null) {
                    log.debug(EMAIL + email + OCCUPIED);
                    isEmailOccupied.set(true);
                } else {
                    User newUser = User.builder()
                                       .email(email)
                                       .password(password)
                                       .role(Roles.DEVELOPER)
                                       .build();
                    userDao.create(newUser);
                    userFromDB.set(newUser);
                }
            });
            if (userFromDB.get() != null) {
                Developer newDeveloper = Developer.builder()
                                             .name(name)
                                             .address(Address.builder()
                                                          .city(city)
                                                          .street(street)
                                                          .building(building)
                                                          .build())
                                             .user(userFromDB.get())
                                             .build();
                developerDao.create(newDeveloper);
                developer.set(newDeveloper);
            }
        });
        if (isEmailOccupied.get()) {
            throw new EmailOccupaidException(EMAIL + email + OCCUPIED);
        } else if (developer.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return developer.get();
    }

    @Override
    public Developer getDeveloper(long userId) throws IOException, RoleException, EntityNotFoundException {

        AtomicReference<Developer> developer = new AtomicReference<>();
        contractorDao.executeInOneVoidTransaction(() -> {
            User user;
            try {
                user = userDao.get(userId);
            } catch (EntityNotFoundException e) {
                log.error(USER_NOT_FOUND_WITH_ID + userId);
                throw e;
            } finally {
                userDao.closeManager();
            }
            if (user != null) {
                if (Roles.DEVELOPER.equals(user.getRole())) {
                    developer.set((Developer) user.getLegalEntity());
                } else {
                    log.error(USER_NOT_DEVELOPER_ID + userId);
                }
            }
        });
        if (developer.get() == null) {
            throw new RoleException(USER_NOT_DEVELOPER);
        }
        return developer.get();
    }

    @Override
    public Page<Project> getMyProjects(long developerId, ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Project> list = new ArrayList<>();
        try {
            long totalCount = projectDao.getCountOfProjectsByDeveloperId(developerId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(projectDao.getProjectsByDeveloperId(developerId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_DEVELOPER_ID + developerId);
        } finally {
            projectDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Page<Contractor> getMyContractors(long developerId, ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Contractor> list = new ArrayList<>();
        try {
            long totalCount = contractorDao.getCountOfContractorsByDeveloperId(developerId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(contractorDao.getContractorsByDeveloperId(developerId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_DEVELOPER_ID + developerId);
        } finally {
            contractorDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Page<Proposal> getAllMyProposals(long developerId, ProposalStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Proposal> list = new ArrayList<>();
        try {
            long totalCount = proposalDao.getCountOfProposalsByDeveloperId(developerId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(proposalDao.getProposalsByDeveloperId(developerId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_DEVELOPER_ID + developerId);
        } finally {
            proposalDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Project createProject(long developerId, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Project> project = new AtomicReference<>();
        projectDao.executeInOneVoidTransaction(() -> {
            Developer developer = null;
            try {
                developer = developerDao.get(developerId);
            } catch (EntityNotFoundException e) {
                log.error(DEVELOPER_NOT_FOUND_WITH_ID + developerId);
            } finally {
                developerDao.closeManager();
            }
            if (developer != null) {
                Project newProject = Project.builder()
                                         .developer(developer)
                                         .name(name)
                                         .address(Address.builder()
                                                      .city(city)
                                                      .street(street)
                                                      .building(building)
                                                      .build())
                                         .build();
                projectDao.create(newProject);
                project.set(newProject);
            }
        });
        if (project.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return project.get();
    }

    @Override
    public void createChapter(long projectId, String name, int price)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Chapter> chapter = new AtomicReference<>();
        chapterDao.executeInOneVoidTransaction(() -> {
            Project project = null;
            try {
                project = projectDao.get(projectId);
            } catch (EntityNotFoundException e) {
                log.error(PROJECT_NOT_FOUND_WITH_ID + projectId);
            } finally {
                projectDao.closeManager();
            }
            if (project != null) {
                Chapter newChapter = Chapter.builder()
                                         .project(project)
                                         .name(name)
                                         .price(price)
                                         .build();
                chapterDao.create(newChapter);
                chapter.set(newChapter);
            }
        });
        if (chapter.get() == null) {
            throw new NotCreateDataInDbException();
        }
    }


    @Override
    public List<Chapter> getChaptersByProjectId(long projectId)
        throws IOException {

        List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getChaptersByProjectId(projectId));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_PROJECT_ID + projectId);
        } finally {
            chapterDao.closeManager();
        }
        return list;
    }

    @Override
    public Page<Chapter> getChaptersByContractorIdAndDeveloperId(
        long developerId, long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Chapter> list = new ArrayList<>();
        try {
            long totalCount = chapterDao.getCountOfChaptersByContractorIdAndDeveloperId(developerId, contractorId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(chapterDao.getChaptersByContractorIdAndDeveloperId(developerId, contractorId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
        } finally {
            chapterDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public void cancelChapter(long chapterId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        chapterDao.executeInOneVoidTransaction(() -> {
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
                isUpdated.set(true);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public void rejectProposal(long proposalId) throws IOException, NotUpdateDataInDbException {

        changeStatusOfProposal(proposalId, ProposalStatus.REJECTED);
    }

    @Override
    public void considerateProposal(long proposalId) throws IOException, NotUpdateDataInDbException {

        changeStatusOfProposal(proposalId, ProposalStatus.CONSIDERATION);
    }

    @Override
    public void approveProposal(long proposalId) throws IOException, NotUpdateDataInDbException {

        changeStatusOfProposal(proposalId, ProposalStatus.APPROVED);
    }

    private void changeStatusOfProposal(long proposalId, ProposalStatus newStatus)
        throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        proposalDao.executeInOneVoidTransaction(() -> {
            Proposal proposal = null;
            try {
                proposal = proposalDao.get(proposalId);
                log.trace(PROPOSAL_READ_FROM_DB + proposal.getId());

            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_PROPOSAL_ID + proposalId);
            }
            if (proposal != null) {
                ProposalStatus currentStatus = proposal.getStatus();

                switch (currentStatus) {
                    case CONSIDERATION:
                        if (ProposalStatus.REJECTED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_CHANGED + proposal.getId() + newStatus);
                            isUpdated.set(true);
                        } else if (ProposalStatus.APPROVED.equals(newStatus)) {

                            Chapter chapter = proposal.getChapter();
                            List<Proposal> proposalList = proposalDao.getProposalsByChapterId
                                                                          (chapter.getId(), ProposalStatus.CONSIDERATION, FIRST_PAGE_NUMBER, Integer.MAX_VALUE);
                            proposalList.forEach(prop -> prop.setStatus(ProposalStatus.REJECTED));
                            proposal.setStatus(ProposalStatus.APPROVED);
                            proposalList.forEach(prop -> {
                                proposalDao.update(prop);
                                if (!prop.getId().equals(proposalId)) {
                                    log.trace(PROPOSAL_STATUS_CHANGED + prop.getId() + ProposalStatus.REJECTED);
                                } else {
                                    log.trace(PROPOSAL_STATUS_CHANGED + prop.getId() + ProposalStatus.APPROVED);
                                }
                            });
                            isUpdated.set(true);
                        }
                        break;
                    case APPROVED:
                        if (ProposalStatus.REJECTED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_CHANGED + proposal.getId() + newStatus);
                            isUpdated.set(true);
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
                                isUpdated.set(true);
                            } else {
                                log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + proposal.getId() + newStatus);
                            }
                        }
                        break;
                }
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }




















    @Override
    public Page<Proposal> getProposalsByChapterId(
        long chapterId, ProposalStatus status, int page, int count)
        throws Exception {

       /* int correctPage = FIRST_PAGE_NUMBER;
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

        Page<Proposal> proposalPagee;

        try {
            proposalPagee = proposalDao.executeInOnePageTransaction(() -> {
                int correctPage = FIRST_PAGE_NUMBER;
                List<Proposal> list = new ArrayList<>();
                try {
                    long totalCount = proposalDao.getCountOfProposalsByChapterId(chapterId, status);
                    correctPage = Util.getCorrectPageNumber(page, count, totalCount);
                    list.addAll(proposalDao.getProposalsByChapterId(chapterId, status, correctPage, count));
                } catch (NoResultException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + chapterId);
                }
                return new Page<>(list, correctPage);
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        return proposalPagee;
    }


    @Override
    public void startProject(long projectId) throws Exception {

        changeProjectStatus(projectId, ProjectStatus.IN_PROCESS);
    }

    @Override
    public void endProject(long projectId) throws Exception {

        changeProjectStatus(projectId, ProjectStatus.COMPLETED);
    }

    @Override
    public void cancelProject(long projectId) throws Exception {

        changeProjectStatus(projectId, ProjectStatus.CANCELED);
    }


    private void changeProjectStatus(long projectId, ProjectStatus newStatus)
        throws Exception {

        boolean isUpdated;
        try {
            isUpdated = projectDao.executeInOneBoolTransaction(() -> {
                Project project = null;
                try {
                    project = projectDao.get(projectId);
                } catch (EntityNotFoundException e) {
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_PROJECT_ID + projectId);
                }
                if (project != null) {
                    switch (project.getStatus()) {
                        case PREPARATION:
                            if (ProjectStatus.IN_PROCESS.equals(newStatus) ||
                                    ProjectStatus.CANCELED.equals(newStatus)) {
                                project.setStatus(newStatus);
                                projectDao.update(project);
                                log.trace(PROJECT_STATUS_CHANGED + project.getId() + newStatus);
                                return true;
                            }
                            break;
                        case IN_PROCESS:
                            if (ProjectStatus.COMPLETED.equals(newStatus) ||
                                    ProjectStatus.CANCELED.equals(newStatus)) {
                                project.setStatus(newStatus);
                                projectDao.update(project);
                                log.trace(PROJECT_STATUS_CHANGED + project.getId() + newStatus);
                                return true;
                            }
                            break;
                    }
                    if (ProjectStatus.CANCELED.equals(newStatus)) {

                        chapterDao.executeInOneVoidTransaction(() -> chapterDao.getChaptersByProjectId(projectId)
                                                                         .forEach(chapter -> {
                                                                             chapter.setStatus(ChapterStatus.CANCELED);
                                                                             chapterDao.update(chapter);
                                                                         }));
                    }
                }
                return false;
            });
        } catch (RollbackException e) {
            log.error(e);
            throw new NotUpdateDataInDbException();
        }
        if (!isUpdated) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Page<Calculation> getCalculationsByChapterId(long chapterId, int page, int count)
        throws Exception {

       /* int correctPage = FIRST_PAGE_NUMBER;
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
                    log.error(THERE_IS_NO_SUCH_DATA_IN_DB_CHAPTER_ID + chapterId);
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
    public void payAdvance(int sum, long calculationId) throws Exception {

        payMoney(sum, calculationId, PaymentType.ADVANCE_PAYMENT);
    }

    @Override
    public void payForWork(int sum, long calculationId) throws Exception {

        payMoney(sum, calculationId, PaymentType.PAYMENT_FOR_WORK);
    }

    private void payMoney(int sum, long calculationId, PaymentType paymentForWork)
        throws Exception {

        boolean isCreated;
        try {
            isCreated = moneyTransferDao.executeInOneBoolTransaction(() -> {
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
                                                    .sum(sum)
                                                    .type(paymentForWork)
                                                    .build();
                    moneyTransferDao.create(newTransfer);
                    return true;
                }
                return false;
            });
        } catch (RollbackException e) {
            log.error(BAD_CONNECTION, e);
            throw new IOException(BAD_CONNECTION);
        }
        if (!isCreated) {
            throw new NotCreateDataInDbException();
        }
    }

    @Override
    public int getProjectDept(Project project) {

        return project.getChapters()
                   .stream()
                   .map(Util::getDebtByChapter)
                   .reduce(ZERO_INT_VALUE, Integer::sum);
    }

    @Override
    public int getTotalDeptByContractor(long contractorId, long developerId) throws IOException {

        int chapterDebt = ZERO_INT_VALUE;
        try {
            chapterDebt = chapterDao.getAllChaptersByDeveloperIdContractorId(developerId, contractorId)
                              .stream()
                              .map(Util::getDebtByChapter)
                              .reduce(ZERO_INT_VALUE, Integer::sum);
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId + AND_DELELOPER_ID + developerId, e);
        } finally {
            chapterDao.closeManager();
        }
        return chapterDebt;
    }
}

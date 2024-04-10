package it.academy.service.impl;

import it.academy.dao.*;
import it.academy.dao.impl.*;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
        developerDao.executeInOneTransaction(() -> {
            AtomicReference<User> userFromDB = new AtomicReference<>();
            userDao.executeInOneTransaction(() -> {
                User user = null;
                try {
                    user = userDao.getUser(email);
                } catch (NoResultException e) {
                    log.debug("Email " + email + " not occupied", e);
                }
                if (user != null) {
                    log.debug("Email " + email + " occupied");
                    isEmailOccupied.set(true);
                } else {
                    User newUser = User.builder()
                                       .email(email)
                                       .password(password)
                                       .role(Roles.DEVELOPER)
                                       .status(UserStatus.AKTIVE)
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
            throw new EmailOccupaidException("Email " + email + " occupied");
        } else if (developer.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return developer.get();
    }

    @Override
    public Developer getDeveloper(Long userId) throws IOException, RoleException, EntityNotFoundException {

        AtomicReference<Developer> developer = new AtomicReference<>();
        contractorDao.executeInOneTransaction(() -> {
            User user;
            try {
                user = userDao.get(userId);
            } catch (EntityNotFoundException e) {
                log.error("User not found with id=" + userId);
                throw e;
            }
            if (user != null) {
                if (Roles.DEVELOPER.equals(user.getRole())) {
                    developer.set((Developer) user.getLegalEntity());
                } else {
                    log.error("User not Developer. id=" + userId);
                }
            }
        });
        if (developer.get() == null) {
            throw new RoleException("User not Developer");
        }
        return developer.get();
    }

    @Override
    public List<Project> getMyProjects(Long developerId, ProjectStatus status, int page, int count)
        throws IOException {

        long totalCount = projectDao.getCountOfProjectsByDeveloperId(developerId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Project> list = new ArrayList<>();
        try {
            list.addAll(projectDao.getProjectsByDeveloperId(developerId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with developerId=" + developerId);
        }
        return list;
    }

    @Override
    public List<Contractor> getMyContractors(Long developerId, ProjectStatus status, int page, int count)
        throws IOException {

        long totalCount = contractorDao.getCountOfContractorsByDeveloperId(developerId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Contractor> list = new ArrayList<>();
        try {
            list.addAll(contractorDao.getContractorsByDeveloperId(developerId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with developerId=" + developerId);
        }
        return list;
    }

    @Override
    public List<Proposal> getAllMyProposals(Long developerId, ProposalStatus status, int page, int count)
        throws IOException {

        long totalCount = proposalDao.getCountOfProposalsByDeveloperId(developerId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Proposal> list = new ArrayList<>();
        try {
            list.addAll(proposalDao.getProposalsByDeveloperId(developerId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with developerId=" + developerId);
        }
        return list;
    }

    @Override
    public void createProject(Long developerId, String name, String city, String street, String building)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Project> project = new AtomicReference<>();
        projectDao.executeInOneTransaction(() -> {
            Developer developer = null;
            try {
                developer = developerDao.get(developerId);
            } catch (EntityNotFoundException e) {
                log.error("Developer not found with id=" + developerId);
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
        project.get();
    }

    @Override
    public void createChapter(Long projectId, String name, Integer price)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Chapter> chapter = new AtomicReference<>();
        chapterDao.executeInOneTransaction(() -> {
            Project project = null;
            try {
                project = projectDao.get(projectId);
            } catch (EntityNotFoundException e) {
                log.error("Project not found with id=" + projectId);
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
    public List<Chapter> getChaptersByProjectId(Long projectId)
        throws IOException {

        List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getChaptersByProjectId(projectId));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with projectId=" + projectId);
        }
        return list;
    }

    @Override
    public List<Chapter> getChaptersByContractorId(Long contractorId, ChapterStatus status, int page, int count)
        throws IOException {

        long totalCount = chapterDao.getCountOfChaptersByContractorId(contractorId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getChaptersByContractorId(contractorId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB with contractorId=" + contractorId);
        }
        return list;
    }

    @Override
    public void cancelChapter(Long chapterId) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        chapterDao.executeInOneTransaction(() -> {
            Chapter chapter = null;
            try {
                chapter = chapterDao.get(chapterId);
                log.trace("Chapter read from DB" + chapter.getId());
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB chapterId=" + chapterId);
            }
            if (chapter != null) {
                chapter.setStatus(ChapterStatus.CANCELED);
                chapterDao.update(chapter);
                log.trace("Chapter status changed " + chapterId + ChapterStatus.CANCELED);
                isUpdated.set(true);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public void rejectProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        changeStatusOfProposal(proposalId, ProposalStatus.REJECTED);
    }

    @Override
    public void approveProposal(Long proposalId) throws IOException, NotUpdateDataInDbException {

        changeStatusOfProposal(proposalId, ProposalStatus.APPROVED);
    }

    private void changeStatusOfProposal(Long proposalId, ProposalStatus newStatus)
        throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        proposalDao.executeInOneTransaction(() -> {
            Proposal proposal = null;
            try {
                proposal = proposalDao.get(proposalId);
                log.trace("Proposal read from DB " + proposal.getId());

            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB proposalId=" + proposalId);
            }
            if (proposal != null) {
                ProposalStatus currentStatus = proposal.getStatus();

                switch (currentStatus) {
                    case CONSIDERATION:
                        if (ProposalStatus.APPROVED.equals(newStatus) ||
                                ProposalStatus.REJECTED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace("Proposal status changed " + proposal.getId() + newStatus);
                            isUpdated.set(true);
                        }
                        break;
                    case APPROVED:
                        if (ProposalStatus.REJECTED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace("Proposal status changed " + proposal.getId() + newStatus);
                            isUpdated.set(true);
                        }
                        break;
                    case REJECTED:
                        if (ProposalStatus.APPROVED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace("Proposal status changed " + proposal.getId() + newStatus);
                            isUpdated.set(true);
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
    public List<Proposal> getProposalsByChapterId(Long chapterId, ProposalStatus status, int page, int count)
        throws IOException {

        long totalCount = proposalDao.getCountOfProposalsByChapterId(chapterId, status);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Proposal> list = new ArrayList<>();
        try {
            list.addAll(proposalDao.getProposalsByChapterId(chapterId, status, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB chapterId=" + chapterId);
        }
        return list;
    }

    @Override
    public void startProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        changeProjectStatus(projectId, ProjectStatus.IN_PROCESS);
    }

    @Override
    public void endProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        changeProjectStatus(projectId, ProjectStatus.COMPLETED);
    }

    @Override
    public void cancelProject(Long projectId) throws IOException, NotUpdateDataInDbException {

        changeProjectStatus(projectId, ProjectStatus.CANCELED);
    }

    private void changeProjectStatus(Long projectId, ProjectStatus newStatus) throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        projectDao.executeInOneTransaction(() -> {
            Project project = null;
            try {
                project = projectDao.get(projectId);
            } catch (EntityNotFoundException e) {
                log.error("There is no such data in DB projectId=" + projectId);
            }
            if (project != null) {

                switch (project.getStatus()) {
                    case PREPARATION:
                        if (ProjectStatus.IN_PROCESS.equals(newStatus) ||
                                ProjectStatus.CANCELED.equals(newStatus)) {
                            project.setStatus(newStatus);
                            projectDao.update(project);
                            log.trace("Project status changed " + project.getId() + newStatus);
                            isUpdated.set(true);
                        }
                        break;
                    case IN_PROCESS:
                        if (ProjectStatus.COMPLETED.equals(newStatus) ||
                                ProjectStatus.CANCELED.equals(newStatus)) {
                            project.setStatus(newStatus);
                            projectDao.update(project);
                            log.trace("Project status changed " + project.getId() + newStatus);
                            isUpdated.set(true);
                        }
                        break;
                }
                if (ProjectStatus.CANCELED.equals(newStatus)) {

                    chapterDao.executeInOneTransaction(() -> chapterDao.getChaptersByProjectId(projectId)
                                                                 .forEach(chapter -> {
                                                                     chapter.setStatus(ChapterStatus.CANCELED);
                                                                     chapterDao.update(chapter);
                                                                 }));
                }
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public List<Calculation> getCalculationsByChapterId(Long chapterId, int page, int count)
        throws IOException {

        long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
        page = Util.getCorrectPageNumber(page, count, totalCount);

        List<Calculation> list = new ArrayList<>();
        try {
            list.addAll(calculationDao.getCalculationsByChapterId(chapterId, page, count));
        } catch (NoResultException e) {
            log.error("There is no such data in DB chapterId=" + chapterId);
        }
        return list;
    }

    @Override
    public void payAdvance(Integer sum, Long calculationId) throws IOException, NotCreateDataInDbException {

        payMoney(sum, calculationId, PaymentType.ADVANCE_PAYMENT);
    }

    @Override
    public void payForWork(Integer sum, Long calculationId) throws IOException, NotCreateDataInDbException {

        payMoney(sum, calculationId, PaymentType.PAYMENT_FOR_WORK);
    }

    private void payMoney(Integer sum, Long calculationId, PaymentType paymentForWork)
        throws IOException, NotCreateDataInDbException {

        AtomicBoolean isCreated = new AtomicBoolean(false);
        AtomicReference<MoneyTransfer> transfer = new AtomicReference<>();
        moneyTransferDao.executeInOneTransaction(() -> {
            Calculation calculation = calculationDao.get(calculationId);
            if (calculation != null) {
                MoneyTransfer newTransfer = MoneyTransfer.builder()
                                                .calculation(calculation)
                                                .sum(sum)
                                                .type(paymentForWork)
                                                .build();
                moneyTransferDao.create(newTransfer);
                transfer.set(newTransfer);
                isCreated.set(true);
            }
        });
        if (!isCreated.get()) {
            throw new NotCreateDataInDbException();
        }
    }

    @Override
    public Integer getProjectDept(Project project) {

        return project.getChapters()
                   .stream()
                   .map(Util::getDebtByChapter)
                   .reduce(0, Integer::sum);
    }

    @Override
    public Integer getTotalDeptByContractor(Long contractorId, Long developerId) throws IOException {

        return chapterDao.getAllChaptersByDeveloperIdContractorId(developerId, contractorId)
                   .stream()
                   .map(Util::getDebtByChapter)
                   .reduce(0, Integer::sum);
    }
}

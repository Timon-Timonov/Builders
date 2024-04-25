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
import it.academy.service.ContractorService;
import it.academy.util.Util;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static it.academy.util.constants.Constants.FIRST_PAGE_NUMBER;
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
        throws IOException, NotCreateDataInDbException, EmailOccupaidException {

        AtomicReference<Contractor> contractor = new AtomicReference<>();
        AtomicBoolean isEmailOccupaid = new AtomicBoolean(false);
        contractorDao.executeInOneTransaction(() -> {
            AtomicReference<User> userFromDB = new AtomicReference<>();
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
                                       .role(Roles.CONTRACTOR)
                                       .status(UserStatus.ACTIVE)
                                       .build();
                    userDao.create(newUser);
                    userFromDB.set(newUser);
                }
            });
            if (userFromDB.get() != null) {
                Contractor newContractor = Contractor.builder()
                                               .name(name)
                                               .address(Address.builder()
                                                            .city(city)
                                                            .street(street)
                                                            .building(building)
                                                            .build())
                                               .user(userFromDB.get())
                                               .build();
                contractorDao.create(newContractor);
                log.trace(CONTRACTOR_CREATED_ID + newContractor.getId());
                contractor.set(newContractor);
            }
        });
        if (isEmailOccupaid.get()) {
            throw new EmailOccupaidException(EMAIL + email + OCCUPIED);
        } else if (contractor.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return contractor.get();
    }

    @Override
    public Contractor getContractor(Long userId) throws IOException, RoleException, EntityNotFoundException {

        AtomicReference<Contractor> contractor = new AtomicReference<>();
        AtomicBoolean roleChecked = new AtomicBoolean(false);
        contractorDao.executeInOneTransaction(() -> {
            User user;
            try {
                user = userDao.get(userId);
            } catch (EntityNotFoundException e) {
                log.error(CONTRACTOR_NOT_FOUND_WITH_ID + userId);
                throw e;
            }
            if (user != null) {
                if (Roles.CONTRACTOR.equals(user.getRole())) {
                    contractor.set((Contractor) user.getLegalEntity());
                    roleChecked.set(true);
                } else {
                    log.error(USER_NOT_CONTRACTOR_ID + userId);
                }
            }
        });
        if (!roleChecked.get()) {
            throw new RoleException(USER_NOT_CONTRACTOR);
        }
        return contractor.get();
    }

    @Override
    public Page<Project> getMyProjects(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Project> list = new ArrayList<>();
        try {
            long totalCount = projectDao.getCountOfProjectsByContractorId(contractorId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(projectDao.getProjectsByContractorId(contractorId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
        } finally {
            projectDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Page<Project> getMyProjectsByDeveloper(
        Long developerId, Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Project> list = new ArrayList<>();
        try {
            long totalCount = projectDao.getCountOfProjectsByDeveloperIdContractorId(
                developerId, contractorId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(projectDao.getProjectsByDeveloperIdContractorId(
                developerId, contractorId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId +
                          AND_DELELOPER_ID + developerId);
        } finally {
            projectDao.closeManager();
        }
        return new Page<>(list, correctPage);
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
    public Page<Chapter> getFreeChapters(Long contractorId, String chapterName, ProjectStatus projectStatus, int page, int count) throws IOException {

        AtomicInteger correctPage = new AtomicInteger(FIRST_PAGE_NUMBER);
        List<Chapter> list = new ArrayList<>();
        try {
            chapterDao.executeInOneTransaction(() -> {
                long totalCount = chapterDao.getCountOfFreeChaptersByName(contractorId, chapterName, projectStatus);
                correctPage.set(Util.getCorrectPageNumber(page, count, totalCount));
                list.addAll(chapterDao.getFreeChapters(contractorId, chapterName, projectStatus, correctPage.get(), count));
            });

        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHPTER_NAME + chapterName);
        }
        return new Page<>(list, correctPage.get());
    }

    @Override
    public Page<Developer> getMyDevelopers(Long contractorId, ProjectStatus status, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Developer> list = new ArrayList<>();
        try {
            long totalCount = developerDao.getCountOfDevelopers(contractorId, status);
            correctPage = Util.getCorrectPageNumber(page, count, totalCount);
            list.addAll(developerDao.getDevelopersByContractorId(contractorId, status, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
        } finally {
            developerDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public Page<Proposal> getMyProposals(Long contractorId, ProposalStatus status, int page, int count)
        throws IOException {

        AtomicInteger correctPage = new AtomicInteger(FIRST_PAGE_NUMBER);
        List<Proposal> list = new ArrayList<>();
        try {
            proposalDao.executeInOneTransaction(() -> {
                long totalCount = proposalDao.getCountOfProposalsByContractorId(contractorId, status);
                correctPage.set(Util.getCorrectPageNumber(page, count, totalCount));
                list.addAll(proposalDao.getProposalsByContractorId(contractorId, status, correctPage.get(), count));
            });

        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
        }
        return new Page<>(list, correctPage.get());
    }

    @Override
    public List<Chapter> getMyChaptersByProjectId(Long projectId, Long contractorId)
        throws IOException {

        List<Chapter> list = new ArrayList<>();
        try {
            list.addAll(chapterDao.getChaptersByProjectIdContractorId(projectId, contractorId));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId +
                          AND_PROJECT_ID + projectId);
        } finally {
            chapterDao.closeManager();
        }
        return list;
    }

    @Override
    public Page<Calculation> getCalculationsByChapter(Long chapterId, int page, int count)
        throws IOException {

        int correctPage = FIRST_PAGE_NUMBER;
        List<Calculation> list = new ArrayList<>();
        try {
            long totalCount = calculationDao.getCountOfCalculationsByChapterId(chapterId);
            correctPage = (Util.getCorrectPageNumber(page, count, totalCount));
            list.addAll(calculationDao.getCalculationsByChapterId(chapterId, correctPage, count));
        } catch (NoResultException e) {
            log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHAPTER_ID + chapterId);
        } finally {
            calculationDao.closeManager();
        }
        return new Page<>(list, correctPage);
    }

    @Override
    public void updateWorkPriceFact(Integer workPriceFact, Long calculationId)
        throws IOException, NotUpdateDataInDbException {

        AtomicBoolean isUpdated = new AtomicBoolean(false);
        calculationDao.executeInOneTransaction(() -> {
            int count = calculationDao.updateWorkPriceFact(workPriceFact, calculationId);
            if (1 == count) {
                log.trace(WORKPRICE_UPDATED_ID + calculationId + VALUE + workPriceFact);
                isUpdated.set(true);
            } else {
                log.debug(WORKPRICE_NOT_UPDATED_ID + calculationId);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Calculation createCalculation(Long chapterId, Integer YYYY, Integer MM, Integer workPricePlan)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Calculation> calculation = new AtomicReference<>();
        calculationDao.executeInOneTransaction(() -> {
            Chapter chapter = null;
            try {
                chapter = chapterDao.get(chapterId);
            } catch (EntityNotFoundException e) {
                log.error(CHAPTER_NOT_FOUND_ID + chapterId);
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
                calculation.set(newCalculation);
            } else {
                log.debug(CALCULATION_NOT_CREATED_CHAPTER_ID + chapterId);
            }
        });
        if (calculation.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return calculation.get();
    }

    @Override
    public void setProposalStatus(Long proposalId, ProposalStatus newStatus) throws IOException, NotUpdateDataInDbException {

        if (proposalId == null || newStatus == null) {
            log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId + NEW_STATUS + newStatus);
            throw new NotUpdateDataInDbException();
        }
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        proposalDao.executeInOneTransaction(() -> {
            Proposal proposal = null;
            try {
                proposal = proposalDao.get(proposalId);
            } catch (EntityNotFoundException e) {
                log.error(PROPOSAL_NOT_FOUND_ID + proposalId);
            }


            if (proposal != null) {

                ProposalStatus oldStatus = proposal.getStatus();

                switch (oldStatus) {
                    case CONSIDERATION:
                        if (ProposalStatus.CANCELED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                            isUpdated.set(true);
                        }
                        break;
                    case APPROVED:
                        if (ProposalStatus.CANCELED.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                            isUpdated.set(true);
                        } else if (ProposalStatus.ACCEPTED_BY_CONTRACTOR.equals(newStatus)) {

                            Chapter chapter = proposal.getChapter();
                            Contractor contractor = proposal.getContractor();

                            chapter.setContractor(contractor);
                            chapter.setStatus(ChapterStatus.OCCUPIED);
                            proposal.setStatus(newStatus);

                            chapterDao.executeInOneTransaction(() -> {
                                chapterDao.update(chapter);
                            });
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                            isUpdated.set(true);
                        }
                        break;
                    case CANCELED:
                        if (ProposalStatus.CONSIDERATION.equals(newStatus)) {
                            proposal.setStatus(newStatus);
                            proposalDao.update(proposal);
                            log.trace(PROPOSAL_STATUS_UPDATE_TO + newStatus.toString());
                            isUpdated.set(true);
                        }
                        break;
                    default:
                        log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId + OLD_STATUS + oldStatus);
                }
            } else {
                log.debug(PROPOSAL_STATUS_NOT_UPDATE_ID + proposalId);
            }
        });
        if (!isUpdated.get()) {
            throw new NotUpdateDataInDbException();
        }
    }

    @Override
    public Proposal createProposal(Long chapterId, Long contractorId)
        throws IOException, NotCreateDataInDbException {

        AtomicReference<Proposal> proposal = new AtomicReference<>();
        proposalDao.executeInOneTransaction(() -> {
            Chapter chapter;
            try {
                chapter = chapterDao.get(chapterId);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CHAPTER_ID + chapterId);
                return;
            } finally {
                chapterDao.closeManager();
            }
            Contractor contractor;
            try {
                contractor = contractorDao.get(contractorId);
            } catch (EntityNotFoundException e) {
                log.error(THERE_IS_NO_SUCH_DATA_IN_DB_WITH_CONTRACTOR_ID + contractorId);
                return;
            } finally {
                contractorDao.closeManager();
            }
            try {
                Proposal proposalFromDB = proposalDao
                                              .getProposalByChapterIdContractorId(chapterId, contractorId);
                proposal.set(proposalFromDB);
                log.debug(DATA_ALREDY_EXIST_IN_DB_ID + proposalFromDB.getId());
                return;
            } catch (NoResultException e) {
                log.trace(THERE_IS_NO_SUCH_PROPOSAL_IN_DB_WITH_CHAPTER_ID + chapterId +
                              AND_CONTRACTOR_ID + contractorId);
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
                proposal.set(newProposal);
            }
        });
        if (proposal.get() == null) {
            throw new NotCreateDataInDbException();
        }
        return proposal.get();
    }

    @Override
    public Integer getTotalDeptByDeveloper(Long contractorId, Long developerId) throws IOException {

        Integer debt = null;
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
